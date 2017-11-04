package daggerok.security;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryAuthenticationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.MapUserDetailsRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

@Slf4j
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

  private static final String ROLE_USER = "USER";
  private static final String ROLE_ADMIN = "ADMIN";
  private static final Map<String, List<String>> USERS = new ConcurrentHashMap<>();

  static {
    USERS.put("user", singletonList(ROLE_USER));
    USERS.put("admin", asList(ROLE_USER, ROLE_ADMIN));
  }

  /**
   * Authorization
   */

  @Bean
  SecurityWebFilterChain springWebFilterChain(HttpSecurity http) throws Exception {
    return http
        .authorizeExchange()
          .pathMatchers("/all/{usernamePathVariable}").access(this::currentUserMatchesRoleAndPath)
          .pathMatchers("/first").access(this::currentUserMatchesRole)
          .pathMatchers("/**").hasRole(ROLE_ADMIN)
        .anyExchange().authenticated()
        .and()
        ////not necessary, already injected
        //.authenticationManager(reactiveAuthenticationManager(userDetailsRepository()))
        .build();
  }

  private Mono<AuthorizationDecision> currentUserMatchesRoleAndPath(Mono<Authentication> authentication, AuthorizationContext context) {
    return authentication
        .map(a -> {
          log.warn("role and path: {}", context.getVariables());
          return a;
        })
        .map(a -> a.getAuthorities()
                   .stream()
                   .map(GrantedAuthority::getAuthority)
                   .anyMatch(r -> r.endsWith(ROLE_USER))
            && a.getName().equals(context.getVariables().get("usernamePathVariable")))
        .map(AuthorizationDecision::new);
  }

  private Mono<AuthorizationDecision> currentUserMatchesRole(Mono<Authentication> authentication, AuthorizationContext context) {
    return authentication
        .map(a -> {
          log.warn("role: {}", a.getAuthorities());
          return a;
        })
        .map(a -> a.getAuthorities()
                   .stream()
                   .map(GrantedAuthority::getAuthority)
                   .anyMatch(r -> r.endsWith(ROLE_USER)))
        .map(AuthorizationDecision::new);
  }

  // @Bean // just role way:
  WebFilter securityFilterChainJustRoleWay(final ReactiveAuthenticationManager reactiveAuthenticationManager) {

    return HttpSecurity.http()
                       .authorizeExchange()
                         .pathMatchers("/**")
                         .hasRole("ADMIN")
                       .and()
                       .httpBasic()
                       .authenticationManager(reactiveAuthenticationManager)
                       .build();
  }

  /**
   * Authentication
   */

  @Bean
  ReactiveAuthenticationManager reactiveAuthenticationManager(final UserDetailsRepository userDetailsRepository) {
    return new UserDetailsRepositoryAuthenticationManager(userDetailsRepository);
  }

  // @Bean
  UserDetailsRepository userDetailsRepositoryTodoNotWorkedYet() {

    return username -> Mono.just(ofNullable(USERS.get(username))
                                     .map(roles -> roles.stream()
                                                        .map(SimpleGrantedAuthority::new)
                                                        .collect(toList()))
                                     .map(authorities -> new User(username, username, authorities))
                                     .orElseThrow(() -> new UsernameNotFoundException(username)));
  }

  @Bean
  UserDetailsRepository userDetailsRepository() {
    val user = User.withUsername("user").password("user").roles(ROLE_USER).build();
    val admin = User.withUsername("admin").password("admin").roles(ROLE_USER, ROLE_ADMIN).build();
    return new MapUserDetailsRepository(user, admin);
  }
}
