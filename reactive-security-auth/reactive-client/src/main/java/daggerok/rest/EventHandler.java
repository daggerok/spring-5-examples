package daggerok.rest;

import daggerok.dto.Movie;
import daggerok.dto.MovieEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM;

@Service
@RequiredArgsConstructor
public class EventHandler {

  final WebClient client;

  public Mono<ServerResponse> stream(final ServerRequest request) {

    return ServerResponse.ok()
                         .contentType(TEXT_EVENT_STREAM)
                         .body(client.get()
                                     .uri("")
                                     .retrieve()
                                     .bodyToFlux(Movie.class)
                                     .flatMap(movie -> client.get()
                                                             .uri("/events/{id}", movie.getId())
                                                             .accept(TEXT_EVENT_STREAM)
                                                             .retrieve()
                                                             .bodyToFlux(MovieEvent.class)),
                               MovieEvent.class);
  }
}
