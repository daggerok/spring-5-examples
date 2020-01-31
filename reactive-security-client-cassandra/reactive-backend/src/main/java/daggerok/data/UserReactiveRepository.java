package daggerok.data;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserReactiveRepository extends ReactiveCrudRepository<User, String> {
  Mono<User> findByUsername(final String username);
}
