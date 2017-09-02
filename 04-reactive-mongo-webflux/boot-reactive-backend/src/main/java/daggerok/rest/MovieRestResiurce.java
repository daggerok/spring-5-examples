package daggerok.rest;

import daggerok.data.Movie;
import daggerok.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
public class MovieRestResiurce {

  final MovieService service;

  @GetMapping
  public Flux<Movie> getMovies() {
    return service.getAll();
  }
}
