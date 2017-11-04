package daggerok.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface MovieReactiveRepository extends CrudRepository<Movie, String> {
//public interface MovieReactiveRepository extends ReactiveCrudRepository<Movie, String> {

  Mono<Movie> findFirstByTitleContainingIgnoreCase(final String title);

  Flux<Movie> findAllByTitleContainingIgnoreCase(final String title);

  default Flux<Movie> findAllItems() {
    return Flux.fromIterable(findAll());
  }
}
