package daggerok;

import daggerok.users.User;
import daggerok.users.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.map.repository.config.EnableMapRepositories;
import reactor.core.publisher.Flux;

import static java.lang.String.format;
import static java.time.LocalDateTime.now;

@Slf4j
@SpringBootApplication
@EnableMapRepositories(considerNestedRepositories = true)
public class MapUsersApplication {

  @Bean
  InitializingBean initializingBean(final UserRepository users) {

    users.deleteAll();

    return () -> Flux.just(1, 2, 3)
                     .map(id -> new User().setUsername(identify(id, "username"))
                                          .setPassword(identify(id, "password"))
                                          .setLastModifiedAt(now()))
                     .map(users::save)
                     .subscribe(user -> log.info("user {} created", user));
  }

  private String identify(final Integer id, final String identifier) {
    return format("%s-%d", identifier, id);
  }

  public static void main(String[] args) {
    SpringApplication.run(MapUsersApplication.class, args);
  }
}
