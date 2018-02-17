package daggerok.github;

import io.vavr.collection.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Map;

import static java.util.Collections.singletonMap;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Slf4j
@Configuration
public class GithubRoutesConfig {

  @Bean
  RouterFunction<ServerResponse> githubRoutes(final GithubProperties props,
                                              final WebClient githubWebClient) {
    return

        route(GET("/github/props/manual"),
              request -> ok().body(Mono.just(
                  singletonMap("github",
                               singletonMap("token", props.getToken()))),
                                   Map.class))

            .andRoute(GET("/github/props/**"),
                      request -> ok().body(Mono.just(props), GithubProperties.class))

            .andRoute(GET("/github/search/users/{username}"), // ?page=1&size=2
                      request -> ok().body(githubWebClient.get()
                                                          .uri(
                                                              "/search/users?q={q}&page={page}&per_page={per_page}",
                                                              HashMap.of(
                                                                  "q", request.pathVariable("username"),
                                                                  "page", request.queryParam("page").orElse("0"),
                                                                  "per_page", request.queryParam("size").orElse("3")
                                                              ).toJavaMap()
                                                          )
                                                          .exchange()
                                                          .subscribeOn(Schedulers.elastic())
                                                          .flatMapMany(response -> response.bodyToFlux(Map.class)),
                                           Map.class))

            .andRoute(GET("/**"),
                      request -> ok().body(Mono.just(singletonMap("result", "TODO")),
                                           Map.class))
        ;
  }
}
