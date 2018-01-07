package daggerok.reservations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.stream.Stream;

import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM;

@Service
@RequiredArgsConstructor
public class HandlerFunctions {

  final ReservationRepository reservations;

  public Mono<ServerResponse> streamReservations(final ServerRequest request) {

    return ServerResponse.ok()
                         .contentType(APPLICATION_STREAM_JSON)
                         .body(dryReservations(), Reservation.class);
  }

  public Mono<ServerResponse> getReservations(final ServerRequest request) {

    return ServerResponse.ok()
                         .contentType(APPLICATION_STREAM_JSON)
                         .body(dryReservations(), Reservation.class);
  }

  public Mono<ServerResponse> getOrderedReservations(final ServerRequest request) {

/*
    final Flux<Long> generate = Flux.fromStream(Stream.generate(System::currentTimeMillis));
    final Flux<Long> interval = Flux.interval(Duration.ofSeconds(1));

    final Flux<Tuple2<Long, Long>> zipper = Flux.zip(generate, interval);
    final Flux<Reservation> maybe = reservations.findWithTailableCursorBy();
*/

    return ServerResponse.ok()
                         .contentType(APPLICATION_STREAM_JSON)
                         .body(reservations.findWithTailableCursorBy()
                                           .share(),
                               Reservation.class);
  }

  private Flux<Reservation> dryReservations() {
    final Flux<Long> interval = Flux.interval(Duration.ofSeconds(1));
    final Flux<Reservation> stream = reservations.findAll();

    return Flux.zip(interval, stream)
               .map(Tuple2::getT2);
  }
}
