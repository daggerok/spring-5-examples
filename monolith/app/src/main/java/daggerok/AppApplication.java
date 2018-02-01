package daggerok;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

import java.io.Serializable;
import java.time.Duration;
import java.util.List;

import static java.lang.String.format;
import static java.util.Collections.singletonList;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Data
@Document
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of")
class Message implements Serializable {

  private static final long serialVersionUID = 7752241147869102059L;

  @Id String id;
  String subject;
  @NonNull String from, body;
  @NonNull List<String> to;
}

interface MessageRepository extends ReactiveMongoRepository<Message, String> {}

/*
@PropertySources({
   @PropertySource("messages.properties"),
   @PropertySource("messages_ru.properties"),
})
*/
/*
@EnableReactiveMongoRepositories(
    considerNestedRepositories = true,
    basePackageClasses = Message.class
)
*/
@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
public class AppApplication {

  final MessageRepository messages;

  @Bean
  InitializingBean initializingBean() {
    return () -> messages.deleteAll()
                         .thenMany(v -> Flux.just(1, 2, 3)
                                            .map(i -> Message.of(
                                                format("from-%d", i),
                                                format("body-%d", i),
                                                singletonList(format("to-%d", i))))
                                            .flatMap(messages::save)
                                            .subscribe(m -> log.info("created\n{}", m)))
                         .subscribe();
  }

  @Bean
  RouterFunction<ServerResponse> routes() {

    return

        route(GET("/api/v1/messages"),
              request -> ok().contentType(TEXT_EVENT_STREAM)
                             //.contentType(APPLICATION_STREAM_JSON)
                             .body(Flux.zip(
                                 Flux.interval(Duration.ofSeconds(1)),
                                 messages.findAll())
                                       .map(Tuple2::getT2)
                                       .share(), Message.class))

            .andRoute(POST("/api/v1/messages"),
                      request -> ok().contentType(APPLICATION_JSON)
                                     .body(request.bodyToMono(Message.class)
                                                  .flatMap(messages::save), Message.class))

            .andRoute(GET("/"),
                      request -> ok().render("index",
                                             Rendering.view("index")
                                                      .modelAttribute("name", "Max")
                                                      .build()
                                                      .modelAttributes())
            );
  }

  public static void main(String[] args) {
    SpringApplication.run(AppApplication.class, args);
  }
}
