package daggerok.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserReactiveRepository extends CrudRepository<User, String> {
  Mono<User> findByUsername(final String username);
}
