package daggerok;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import daggerok.reservations.Reservation;
import daggerok.reservations.ReservationRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.query.Collation;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@SpringBootApplication
@EnableReactiveMongoRepositories(considerNestedRepositories = true)
public class ReservationsApplication {

  @Bean
  InitializingBean initializingBean(final ReservationRepository reservations,
                                    final MongoOperations ops) {

    final AtomicLong atomicLong = new AtomicLong(1);
    final LocalDate base = LocalDate.now();

    if (ops.collectionExists(Reservation.class))
      ops.dropCollection(Reservation.class);

    ops.createCollection(Reservation.class, new CollectionOptions(1_000_000_000_000_000L, null, true));

    return () -> Flux.just(1, 2, 3)
                     .map(days -> new Reservation().setCheckIn(base.plusDays(atomicLong.incrementAndGet()))
                                                   .setCheckOut(base.plusDays(atomicLong.incrementAndGet())))
                     .flatMap(reservations::save)
                     .subscribe(r -> {
                       log.info("created: {}", r);
                       ops.executeCommand("db.runCommand({ convertToCapped: 'reservation', size: 1111111111 })");
                     });

/*
    return () -> Flux.just(1, 2)
                     .map(String::valueOf)
                     .map(string -> new Reservation().setCheckIn(LocalDate.now())
                                                     .setCheckOut(LocalDate.now().plusDays(1)))
                     .flatMap(reservations::save)
                     .subscribe(r -> log.info("created: {}", r));
*/
/*
    return () -> reservations.deleteAll()
                             .thenMany(
                                 Flux.just(1, 2)
                                     .map(String::valueOf)
                                     .map(string -> new Reservation().setCheckIn(LocalDate.now())
                                                                     .setCheckOut(LocalDate.now().plusDays(1)))
                                     .flatMap(reservations::save))
                             .subscribe(r -> log.info("created: {}", r));
*/
  }

  public static void main(String[] args) {
    SpringApplication.run(ReservationsApplication.class, args);
  }
}
