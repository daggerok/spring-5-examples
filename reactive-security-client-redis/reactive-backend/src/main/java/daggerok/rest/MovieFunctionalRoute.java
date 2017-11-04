package daggerok.rest;

import daggerok.data.Movie;
import daggerok.data.MovieReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Slf4j
@Configuration
public class MovieFunctionalRoute {

  @Bean
  RouterFunction<ServerResponse> movieRouterFunction(final MovieHandler handler, final MovieReactiveRepository repo) {

    return route(GET("/first"), handler::getFirstLike)
        .andRoute(GET("/all"), handler::searchAll)
        .andRoute(GET("/{id}"), handler::getMovie)
        .andRoute(GET("/events/{id}"), handler::eventStream)
        .andRoute(GET("/**"), handler::getMovies)
        .andRoute(POST("/"),
                  request -> ServerResponse.accepted().body(
                      subscriber -> request.bodyToFlux(Movie.class).flatMap(
                          movie -> Mono.just(repo.save(movie))), Movie.class))
        ;
  }
}
