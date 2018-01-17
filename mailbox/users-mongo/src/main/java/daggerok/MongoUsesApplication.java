package daggerok;

import daggerok.users.User;
import daggerok.users.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import reactor.core.publisher.Flux;

import static java.lang.String.format;
import static java.time.LocalDateTime.now;

@Slf4j
@SpringBootApplication
@EnableReactiveMongoRepositories(considerNestedRepositories = true)
public class MongoUsesApplication {

  @Bean
  InitializingBean initializingBean(final UserRepository users) {

    return () -> users.deleteAll()
                      .thenMany(v -> Flux.just(1, 2, 3)
                                         .map(id -> new User().setUsername(identify(id, "username"))
                                                              .setPassword(identify(id, "password"))
                                                              .setLastModifiedAt(now()))
                                         .flatMap(users::save)
                                         .subscribe(u -> log.info("created {}", u)))
                      .log("----")
                      .subscribe();
  }

  private String identify(final Integer id, final String identifier) {
    return format("%s-%d", identifier, id);
  }

  public static void main(String[] args) {
    SpringApplication.run(MongoUsesApplication.class, args);
  }
}
