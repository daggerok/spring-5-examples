package com.github.daggerok;

import io.vavr.collection.HashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;

import static java.lang.String.format;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
@RequiredArgsConstructor
class Handlers {

  private final WebClient webClient;

  private static final ServerResponse.BodyBuilder jsonBuilder = ok().contentType(APPLICATION_JSON);

  public Mono<ServerResponse> hey(ServerRequest request) {
    return jsonBuilder.body(Mono.just("hey!"), String.class);
  }

  public Mono<ServerResponse> hoy(ServerRequest request) {
    return jsonBuilder.body(Mono.just("hoy!"), String.class);
  }

  public Mono<ServerResponse> falback(ServerRequest request) {
    final URI uri = request.uri();
    final String scheme = uri.getScheme();
    final String authority = uri.getAuthority();
    final String baseUrl = format("%s://%s", scheme, authority);
    final Map<String, String> api = HashMap.of("hey", format("%s/hey", baseUrl),
                                               "hoy", format("%s/hoy", baseUrl),
                                               "collect", format("%s/collect", baseUrl))
                                           .toJavaMap();
    return jsonBuilder.body(Mono.just(api), Map.class);
  }

  public Mono<ServerResponse> collect(ServerRequest request) {
    return jsonBuilder.body(
        Mono.zip(
            webClient.get().uri("/api/v1/hey").retrieve()
                     .bodyToMono(String.class)
                     .map(h -> format("HEY response: %s", h)),
            webClient.get().uri("/api/v1/hoy").retrieve()
                     .bodyToMono(String.class)
                     .map(h -> format("HOY response: %s", h)))
            .map(tuple2 -> format("%s - %s", tuple2.getT1(), tuple2.getT2())),
        String.class);
  }
}

@Slf4j
@SpringBootApplication
public class App {

  @Value("${server.port:${local.server.port:8080}}")
  private Integer port;

  @Bean
  WebClient webClient() {
    return WebClient.builder().baseUrl(format("http://127.0.0.1:%d", port)).build();
  }

  @Bean
  RouterFunction<ServerResponse> routes(Handlers handlers) {
    return nest(path("/api/v1"), route()
        .GET("/hey", handlers::hey)
        .GET("/hoy", handlers::hoy)
        .GET("/collect", handlers::collect)
        .build())
        .andRoute(path("/**"), handlers::falback);
  }

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }
}
