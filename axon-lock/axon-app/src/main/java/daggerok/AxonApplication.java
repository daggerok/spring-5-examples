package daggerok;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.scheduler.Schedulers;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.created;

@Slf4j
@SpringBootApplication
public class AxonApplication {

  public static void main(String[] args) {
    SpringApplication.run(AxonApplication.class, args);
  }

  @Configuration
  static class RouterConfig {

    @Bean RouterFunction<ServerResponse> routes() {

      return

          route(POST("/api/v1/register-guest"), request -> {

            final String uuid = UUID.randomUUID().toString();

            final URI uri = request.uriBuilder()
                                   .path(uuid)
                                   .build();

            return created(uri).body(request.bodyToMono(Map.class)
                                            .map(map -> map.get("name"))
                                            .map(name -> {

                                              log.info("create user {}", name);
                                              return "";

                                            }).subscribeOn(Schedulers.elastic()), String.class);

          })

          ;
    }
  }
}
