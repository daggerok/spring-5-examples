package daggerok;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

import static java.lang.String.format;
import static java.util.Collections.singletonMap;
import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.accepted;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Slf4j
@SpringBootApplication
public class FluxSinkApplication {

  @Configuration
  public static class SubscriptionsConfig {

    @Bean
    List<FluxSink<ServerSentEvent<Map>>> subscribers() {
      return new CopyOnWriteArrayList<>();
    }

    @Bean
    Flux<ServerSentEvent<Map>> processor(final List<FluxSink<ServerSentEvent<Map>>> subscribers) {
      return Flux.create(
          fluxSink -> subscribers.add(
              fluxSink.onCancel(() -> subscribers.remove(fluxSink))
                      .onDispose(() -> log.debug("disposing..."))
                      .onRequest(i -> log.debug("{} subscribers on request", subscribers.size()))));
    }

    @Bean
    Consumer<String> distributeEvent(final List<FluxSink<ServerSentEvent<Map>>> subscribers) {
      return message -> subscribers.forEach(fluxSink -> fluxSink.next(ServerSentEvent.<Map>builder()
                                                                          .id(UUID.randomUUID().toString())
                                                                          .data(singletonMap("payload", message))
                                                                          //.data(HashMap.of(
                                                                          //    "payload", message,
                                                                          //    "at", Instant.now()
                                                                          //).toJavaMap())
                                                                          .event("message")
                                                                          .build()));
    }
  }

  @Configuration
  @RequiredArgsConstructor
  public static class HandlerConfig {

    final List<FluxSink<ServerSentEvent<Map>>> subscribers;

    @Bean
    Consumer<ServerSentEvent<Map>> onNextConsumer() {
      return payload -> log.info("({}) payload: {}", subscribers.size(), payload);
    }

    @Bean
    Consumer<Throwable> onErrorConsumer() {
      return err -> log.error("({}) f*ck... {}", subscribers.size(), err.getLocalizedMessage());
    }

    @Bean
    Runnable completeConsumer() {
      return () -> log.debug("({}) complete.", subscribers.size());
    }

    @Bean
    ApplicationRunner handler(final Flux<ServerSentEvent<Map>> processor,
                              final Consumer<ServerSentEvent<Map>> onNextConsumer,
                              final Consumer<Throwable> onErrorConsumer,
                              final Runnable completeConsumer) {

      return args -> processor.subscribe(
          onNextConsumer,
          onErrorConsumer,
          completeConsumer
      );
    }
  }

  @Bean
  RouterFunction<ServerResponse> routes(final Flux<ServerSentEvent<Map>> processor,
                                        final Consumer<String> distributeEvent) {

    final ParameterizedTypeReference<Map<String, String>> type
        = new ParameterizedTypeReference<Map<String, String>>() {};

    return

        route(GET("/**"),
              request -> ok().contentType(request.headers().accept().contains(TEXT_EVENT_STREAM)
                                              ? TEXT_EVENT_STREAM : APPLICATION_STREAM_JSON)
                             .body(processor.map(s -> s), ServerSentEvent.class))

            .andRoute(POST("/**"),
                      request -> accepted().body(request.bodyToMono(type)
                                                        .map(map -> map.getOrDefault("message", ""))
                                                        .map(String::valueOf)
                                                        .map(String::trim)
                                                        .filter(s -> s.length() > 0)
                                                        .doOnNext(distributeEvent)
                                                        .map(m -> format("message '%s' accepted.", m))
                                                        .map(message -> singletonMap("response", message))
                                                        .subscribeOn(Schedulers.elastic())
                                                        .flatMap(Mono::just), Map.class))
        ;
  }

  public static void main(String[] args) {
    SpringApplication.run(FluxSinkApplication.class, args);
  }
}
