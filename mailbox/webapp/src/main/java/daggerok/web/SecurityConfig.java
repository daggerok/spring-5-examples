package daggerok.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {

  @Bean SecurityWebFilterChain springSecurityFilterChain(final ServerHttpSecurity http) {

    http
        .authorizeExchange()
          .pathMatchers("/favicon.ico")
            .permitAll()
          .anyExchange()
            .authenticated()
            .and()
        .httpBasic()
          .and()
        .formLogin();

    return http.build();
  }

  @Bean MapReactiveUserDetailsService userDetailsService() {
    return new MapReactiveUserDetailsService(
        User.withUsername("max")
            .password("max")
            .roles("USER")
            .build()
    );
  }

  @Bean PasswordEncoder passwordEncoder() {
    return NoOpPasswordEncoder.getInstance();
  }
}
