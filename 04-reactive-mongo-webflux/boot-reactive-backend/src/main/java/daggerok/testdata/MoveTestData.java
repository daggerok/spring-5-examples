package daggerok.testdata;

import daggerok.data.Movie;
import daggerok.data.MovieReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.stream.Stream;

@Slf4j
@Configuration
public class MoveTestData {

  @Bean
  public CommandLineRunner init(final MovieReactiveRepository repository) {

    val publisher = Flux.fromStream(Stream.of(new Movie("ololo"),
                                              new Movie("trololo")))
                        .map(repository::saveIfNew);

    return args -> publisher.subscribe(null, null, () -> {
      repository.findAll().subscribe(movie -> log.info("\n{}", movie));
    });
  }
}
