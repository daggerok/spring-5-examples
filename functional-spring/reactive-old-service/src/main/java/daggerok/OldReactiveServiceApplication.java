package daggerok;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static java.util.Collections.singletonMap;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Slf4j
@SpringBootApplication
public class OldReactiveServiceApplication {

  @Bean
  public RouterFunction<ServerResponse> routes() {

    return

        route(
            GET("/other/"),
            request -> ok().body(Mono.just("hi"), String.class))

        .andRoute(
            GET("/other/{name}"),
            request -> ok().body(Mono.just("hello, " + request.pathVariable("name") + "!"), String.class))

        .andRoute(
            GET("/**"),
            request -> ok().body(Mono.just("fallback"), String.class))
        ;
  }
/* // not needed anymore
  @Bean
  public ReactorHttpHandlerAdapter adapter() {

    val handler = RouterFunctions.toHttpHandler(routes());
    return new ReactorHttpHandlerAdapter(handler);
  }

  @PostConstruct
  public void start() {

    val nettyPort = 8000 + new Random().nextInt(10000);
    val httpServer = HttpServer.create(nettyPort);

    httpServer.newHandler(adapter())
              .subscribe(netty -> Try.of(System.in::read));
  }
*/
  public static void main(String[] args) {

    new SpringApplicationBuilder(OldReactiveServiceApplication.class)
        .properties(singletonMap("server.port", "8000"))
        .run(args);
  }
}
