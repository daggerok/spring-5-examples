package daggerok.testdata;

import daggerok.data.Movie;
import daggerok.data.MovieReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
public class MoveTestData {

  @Autowired MovieReactiveRepository repository;

  @Bean
  @Transactional
  public CommandLineRunner init() {

//    return args -> log.info("repo: {}", repository.findAll());
///*
    Flux.from(s -> repository.deleteAll())
                             .thenMany(Flux.just("ololo", "trololo")
                                           .map(Movie::new)
                                           .flatMap(movie -> Mono.just(repository.save(movie))))
                             .subscribe(null, null, () -> {
                                 log.info("oO");
                                 repository.findAllItems().subscribe(movie -> log.info("\n{}", movie));
                             });
//*/
    return args -> log.info("{}", repository.findAll());
  }
}
