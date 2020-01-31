package daggerok.rest;

import daggerok.data.Movie;
import daggerok.data.MovieReactiveRepository;
import daggerok.service.MovieEvent;
import daggerok.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM;

@Slf4j
@Component
@RequiredArgsConstructor
public class MovieHandler {

  final MovieService movieService;
  final MovieReactiveRepository repo;

  public Mono<ServerResponse> getFirstLike(final ServerRequest request) {

    val searchCriteria = request.queryParam("q").orElse("");
    val body = movieService.getFirstLike(searchCriteria);

    return ServerResponse.ok().body(body, Movie.class);
  }

  public Mono<ServerResponse> searchAll(final ServerRequest request) {

    val searchCriteria = request.queryParam("q").orElse("");
    val body = movieService.getLike(searchCriteria);

    return ServerResponse.ok().body(body, Movie.class);
  }

  public Mono<ServerResponse> getMovie(final ServerRequest request) {

    val id = request.pathVariable("id");
    val body = movieService.getById(id);

    return ServerResponse.ok().body(body, Movie.class);
  }

  public Mono<ServerResponse> eventStream(final ServerRequest request) {

    val id = request.pathVariable("id");
    val body = movieService.streamEvents(id);

    return ServerResponse.ok()
                         .contentType(TEXT_EVENT_STREAM)
                         .body(body, MovieEvent.class);
  }

  public Mono<ServerResponse> getMovies(final ServerRequest request) {

    repo.saveAll(Stream.of("one").map(Movie::new).collect(Collectors.toList()));
    log.info("{}", repo.findAll());

    return ServerResponse.ok().body(movieService.getAll(), Movie.class);
  }
}
