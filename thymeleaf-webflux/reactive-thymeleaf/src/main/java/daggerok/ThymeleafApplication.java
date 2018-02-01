package daggerok;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.Rendering;
import org.thymeleaf.spring5.ISpringWebFluxTemplateEngine;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import org.thymeleaf.spring5.view.reactive.ThymeleafReactiveViewResolver;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

import java.time.Duration;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@SpringBootApplication
@PropertySource("classpath:messages.properties")
@EnableConfigurationProperties(ThymeleafProperties.class)
public class ThymeleafApplication {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor(staticName = "of")
  static class Message {
    String payload;
  }

///*
  @Controller
  static class IngexPage {

    @GetMapping
    Rendering index() {

      return Rendering.view("index")
                      .modelAttribute("message", Message.of("hello, la-la!"))
                      .modelAttribute("messages", new ReactiveDataDriverContextVariable(
                          Flux.zip(
                              Flux.interval(Duration.ofSeconds(1)),
                              Flux.just(
                                  Message.of("and one"),
                                  Message.of("and two"),
                                  Message.of("and three"),
                                  Message.of("and four!"))
                          ).map(Tuple2::getT2),
                          1
                      ))
                      .build();
    }
  }
//*/
//
/*
  @Bean
  RouterFunction<ServerResponse> routes() {

    return

        route(GET("/**"), // reactive-style
              request -> ok().render(
                  "index",
                  Rendering.view("index")
                           .modelAttribute("message", Message.of("restactive! (:"))
                           .modelAttribute(
                               "messages",
                               new ReactiveDataDriverContextVariable(
                                   Flux.zip(
                                       Flux.interval(Duration.ofSeconds(1)),
                                       Flux.just(
                                           Message.of("he-he"),
                                           Message.of("ho-ho"),
                                           Message.of("hi-hi!")
                                       )
                                   ).map(Tuple2::getT2),
                                   1
                               )
                           )
                           .build()
                           .modelAttributes()
              )
        )
        ;
  }
*/
  public static void main(String[] args) {
    SpringApplication.run(ThymeleafApplication.class, args);
  }
}
