package com.github.daggerok;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor(staticName = "of")
class Person {
  private String id;
  @NonNull
  private String name;
}

class PersonRepository {

  private final Map<String, Person> db = new ConcurrentHashMap<>();

  public Flux<Person> allPeople() {
    return Flux.fromIterable(db.values());
  }

  public Mono<Person> savePerson(Mono<Person> person) {
    return person.map(p -> {
      String maybeId = p.getId();
      String id = Objects.isNull(maybeId) || maybeId.trim().isEmpty()
          ? UUID.randomUUID().toString() : maybeId;
      db.put(id, Person.of(id, p.getName()));
      return db.get(id);
    });
  }

  public Mono<Person> getPerson(String personId) {
    return Mono.justOrEmpty(db.get(personId));
  }

  public Mono<Void> deleteAll() {
    return Mono.empty();
  }
}

class PersonHandler {

  private final PersonRepository repository;

  public PersonHandler(PersonRepository repository) {
    this.repository = repository;
  }

  public Mono<ServerResponse> listPeople(ServerRequest request) {
    Flux<Person> people = repository.allPeople();
    return ok().contentType(APPLICATION_JSON)
               .body(people, Person.class);
  }

  public Mono<ServerResponse> createPerson(ServerRequest request) {
    Mono<Person> person = request.bodyToMono(Person.class);
    Mono<Person> saved = repository.savePerson(person);
    return ok().body(saved, Person.class);
  }

  public Mono<ServerResponse> getPerson(ServerRequest request) {
    String personId = request.pathVariable("id");
    return repository.getPerson(personId)
                     .flatMap(person -> ok().contentType(APPLICATION_JSON)
                                            .body(fromObject(person)))
                     .switchIfEmpty(ServerResponse.notFound().build());
  }
}

@SpringBootApplication
public class FnApplication {

  @Bean
  RouterFunction<ServerResponse> routes() {
    PersonRepository repository = new PersonRepository();
    PersonHandler handler = new PersonHandler(repository);
    return route().GET("/person/{id}", accept(APPLICATION_JSON), handler::getPerson)
                  .GET("/person", accept(APPLICATION_JSON), handler::listPeople)
                  .POST("/person", handler::createPerson)
                  .build()
                  .andRoute(path("/**"), handler::listPeople);
  }

  public static void main(String[] args) {
    SpringApplication.run(FnApplication.class, args);
  }
}
