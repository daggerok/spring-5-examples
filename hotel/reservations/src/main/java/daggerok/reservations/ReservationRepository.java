package daggerok.reservations;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import reactor.core.publisher.Flux;

public interface ReservationRepository extends ReactiveMongoRepository<Reservation, String> {

  @Tailable
  Flux<Reservation> findWithTailableCursorBy();
}
