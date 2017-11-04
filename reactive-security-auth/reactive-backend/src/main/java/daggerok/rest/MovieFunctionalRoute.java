package daggerok.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class MovieFunctionalRoute {

  @Bean
  RouterFunction<ServerResponse> movieRouterFunction(final MovieHandler handler) {

    return route(GET("/first"), handler::getFirstLike)
        .andRoute(GET("/all"), handler::searchAll)
        .andRoute(GET("/{id}"), handler::getMovie)
        .andRoute(GET("/events/{id}"), handler::eventStream)
        .andRoute(GET("/**"), handler::getMovies)
        ;
  }
}
