package daggerok.github;

import io.vavr.collection.HashMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class GithubPropertiesRoutes {

  @Bean
  RouterFunction<ServerResponse> githubRoutes(final GithubProperties props) {

    return

        route(GET("/github/**"),
              request -> ok().body(Mono.just(props), GithubProperties.class))

            .andRoute(GET("/**"),
                      request -> ok().body(Mono.just(HashMap.of("result", "TODO")
                                                            .toJavaMap()),
                                           Map.class))

        ;
  }
}
