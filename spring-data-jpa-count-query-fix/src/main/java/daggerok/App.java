package daggerok;

import daggerok.data.Order;
import daggerok.data.OrderNumber;
import daggerok.data.OrderRepository;
import daggerok.data.Price;
import io.vavr.Tuple;
import io.vavr.collection.HashMap;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Map;

import static java.lang.String.format;
import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
@RequiredArgsConstructor
class DbInitializerConfig {

  private final OrderRepository orderRepository;

  //1:
  @Bean
  InitializingBean initDB() {
    return () -> HashMap.of(123, "ololo",
                            456, "trololo")
                        .map((orderNumber, description) -> Tuple.of(BigDecimal.valueOf(orderNumber * System.currentTimeMillis() / 1234567890.987654321),
                                                                    singletonList(description)))
                        .map(t -> Order.of(OrderNumber.of(t._1().intValue()), Price.of(t._1())))
                        .forEach(orderRepository::save);
  }
}

@Configuration
@RequiredArgsConstructor
class WebfluxRoutesConfig {

  private final OrderRepository orderRepository;

  @Bean
  HandlerFunction<ServerResponse> getCountQueryHandler() {
    return request ->
        ok().contentType(APPLICATION_JSON_UTF8)
            .body(Flux.fromIterable(orderRepository.findAllPrices()), Price.class);
  }

  @Bean
  HandlerFunction<ServerResponse> getOrdersHandler() {
    return request ->
        ok().contentType(APPLICATION_JSON_UTF8)
            .body(Flux.fromIterable(orderRepository.findAll()), Order.class);
  }

  @Bean
  HandlerFunction<ServerResponse> fallbackHandler() {
    return request -> {
      final URL url = Try.of(() -> request.uri().toURL())
                         .getOrElseThrow(() -> new RuntimeException("=/"));
      final String protocol = url.getProtocol();
      final int defaultPort = "https".equals(protocol) ? 443 : 80;
      final int currentPort = url.getPort();
      final int port = currentPort == -1 ? defaultPort : currentPort;
      final String baseUrl = format("%s://%s:%d", protocol, url.getHost(), port);
      return ok().body(Flux.just(
          format("GET orders -> %s/api/orders/", baseUrl),
          format("GET pages -> %s/api/", baseUrl),
          format("GET ** -> %s/", baseUrl)
      ), String.class);
    };
  }

  @Bean
  RouterFunction routes(final HandlerFunction<ServerResponse> fallbackHandler) {
    return
        nest(
            path("/"),
            nest(
                accept(APPLICATION_JSON),
                route(
                    GET("/api/orders"),
                    getOrdersHandler()
                )
            ).andNest(
                accept(APPLICATION_JSON),
                route(
                    GET("/api"),
                    getCountQueryHandler()
                )
            )
        ).andOther(
            route(
                GET("/**"),
                fallbackHandler
            )
        )
        ;
  }
}

@SpringBootApplication
public class App {

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }
}
