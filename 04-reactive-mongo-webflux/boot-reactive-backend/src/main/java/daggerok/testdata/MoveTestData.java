package daggerok.testdata;

import daggerok.data.Movie;
import daggerok.data.MovieReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

@Slf4j
@Configuration
public class MoveTestData {

  @Bean
  @Transactional
  public CommandLineRunner init(final MovieReactiveRepository repository) {

    return args -> repository.deleteAll()
                             .thenMany(Flux.just("ololo", "trololo")
                                           .map(Movie::new)
                                           .flatMap(repository::save))
                             .subscribe(null, null, () ->
                                 repository.findAll().subscribe(movie -> log.info("\n{}", movie)));
  }
}
