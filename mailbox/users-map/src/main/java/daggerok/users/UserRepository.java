package daggerok.users;

import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository extends KeyValueRepository<User, String>,
    PagingAndSortingRepository<User, String> {

  default Flux<User> findAny() {
    return Flux.fromIterable(findAll());
  }

  default Mono<User> saveOne(final User user) {
    return Mono.just(save(user));
  }
}
