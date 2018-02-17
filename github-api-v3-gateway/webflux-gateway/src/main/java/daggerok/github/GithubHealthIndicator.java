package daggerok.github;

import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Slf4j
@Component
@RequiredArgsConstructor
public class GithubHealthIndicator implements HealthIndicator {

  final WebClient githubWebClient;

  @Override
  public Health health() {

    final BiConsumer<ClientResponse, Throwable> errorLogger = (clientResponse, throwable) -> {
      if (!clientResponse.statusCode().is2xxSuccessful())
        log.error("github health status: {}", clientResponse.statusCode());
      if (throwable != null)
        log.error("github health error: {}", throwable.getLocalizedMessage(), throwable);
    };

    final Function<Throwable, CompletableFuture<HttpStatus>> badRequest = e -> Mono.just(HttpStatus.BAD_REQUEST)
                                                                                   .toFuture();
    final CompletableFuture<HttpStatus> status = Try.of(() -> githubWebClient.get()
                                                                             .exchange()
                                                                             .doOnSuccessOrError(errorLogger)
                                                                             .map(ClientResponse::statusCode)
                                                                             .toFuture())
                                                    .getOrElseGet(badRequest);

    final Function<Boolean, Health.Builder> healthBuilder = bool -> bool ? Health.up() : Health.down();

    final Function<Throwable, Health.Builder> throwableBuilder = e -> Health.down(new RuntimeException(e));

    final CompletableFuture<Health> subscription = status.thenApply(HttpStatus::is2xxSuccessful)
                                                         .thenApply(healthBuilder)
                                                         .exceptionally(throwableBuilder)
                                                         .thenApply(Health.Builder::build);

    final Function<Throwable, Health> health = e -> throwableBuilder.apply(e)
                                                                    .build();
    return Try.of(subscription::get)
              .getOrElseGet(health);
  }
}
