package daggerok.data;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface MovieReactiveRepository extends ReactiveMongoRepository<Movie, String> {

  Mono<Movie> findFirstByTitleContainingIgnoreCase(final String title);

  Flux<Movie> findAllByTitleContainingIgnoreCase(final String title);
}
