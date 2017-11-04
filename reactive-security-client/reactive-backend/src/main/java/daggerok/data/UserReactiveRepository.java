package daggerok.data;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserReactiveRepository extends ReactiveMongoRepository<User, String> {
  Mono<User> findByUsername(final String username);
}
