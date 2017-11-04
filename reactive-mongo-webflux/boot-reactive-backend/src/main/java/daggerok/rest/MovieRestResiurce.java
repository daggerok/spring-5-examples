package daggerok.rest;

import daggerok.data.Movie;
import daggerok.service.MovieEvent;
import daggerok.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

@RestController
@RequiredArgsConstructor
public class MovieRestResiurce {

  final MovieService service;

  @GetMapping("/first")
  public Mono<Movie> searchFirst(@RequestParam("q") final String searchCriteria) {
    return service.getFirstLike(searchCriteria);
  }

  @GetMapping("/all")
  public Flux<Movie> searchAll(@RequestParam("q") final String searchCriteria) {
    return service.getLike(searchCriteria);
  }

  @GetMapping("/{id}")
  public Mono<Movie> getMovie(@PathVariable("id") final String id) {
    return service.getById(id);
  }

  @GetMapping(path = "/events/{id}", produces = TEXT_EVENT_STREAM_VALUE)
  public Flux<MovieEvent> eventStream(@PathVariable("id") final String id) {
    return service.streamEvents(id);
  }

  @GetMapping
  public Flux<Movie> getMovies() {
    return service.getAll();
  }
}
