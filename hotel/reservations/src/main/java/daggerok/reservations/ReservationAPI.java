package daggerok.reservations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

@Configuration
public class ReservationAPI {

  @Bean
  RouterFunction<ServerResponse> routes(final HandlerFunctions handlers) {

    return RouterFunctions

        .route(GET("/api/v1/reservations/stream"),
               handlers::getOrderedReservations)

        .andRoute(GET("/api/v1/reservations/**"),
                  handlers::streamReservations)
        ;
  }
}
