package daggerok.security;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.core.userdetails.MapUserDetailsRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsRepository;

@Slf4j
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

  @Bean // TODO: Implement production ready solution for user details...
  UserDetailsRepository userDetailsRepository() {
    val user = User.withUsername("user").password("user").roles("USER").build();
    return new MapUserDetailsRepository(user);
  }

/*
  @Autowired
  UserReactiveRepository repository;

  @PostConstruct
  @Transactional
  public void init() {

    repository.deleteAll()
              .thenMany(Flux.just("user", "admin")
                            .map(s -> new User().setUsername(s)
                                                .setPassword(s)
                                                .setEnabled(true)
                                                .setAuthorities(s.contains("admin") ? ADMIN : USER))
                            .flatMap(repository::save))
              .subscribe(null, null, () ->
                  repository.findAll().subscribe(movie -> log.info("\n{}", movie)));
  }

  @Bean
  UserDetailsRepository userDetailsRepository() {
    return new MapUserDetailsRepository(repository.findAll().toStream().collect(toList()));
  }
*/
}
