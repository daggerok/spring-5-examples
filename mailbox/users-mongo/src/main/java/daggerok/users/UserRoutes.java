package daggerok.users;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Service
@RequiredArgsConstructor class UserHandlers {

  final UserRepository users;

  public Mono<ServerResponse> streamUsers(final ServerRequest inore) {

    return ServerResponse.ok()
                         .contentType(APPLICATION_STREAM_JSON)
                         .body(users.findAll(), User.class);
  }

  public Mono<ServerResponse> saveUser(final ServerRequest request) {

    return ServerResponse.ok()
                         .contentType(APPLICATION_STREAM_JSON)
                         .body(request.bodyToFlux(User.class)
                                      .flatMap(users::save), User.class);
  }
}

@Configuration
public class UserRoutes {

  @Bean
  RouterFunction<ServerResponse> routes(final UserHandlers handlers) {

    return route(GET("/api/v1/users/**"),
                 handlers::streamUsers)
        .andRoute(POST("/api/v1/users/**"),
                  handlers::saveUser)
        ;
  }
}
