package daggerok.config;

import daggerok.rest.EventHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ClientRouterFunctionConfig {

  @Bean
  public RouterFunction<ServerResponse> clientRouter(final EventHandler handler) {
    return route(GET("/stream"), handler::stream);
  }
}

