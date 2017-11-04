package daggerok.rest;

import daggerok.dto.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
public class ClientResource {

  final WebClient client;

  @GetMapping
  public Flux<Movie> all() {

    return client.get()
                 .retrieve()
                 .bodyToFlux(Movie.class);
  }
}
