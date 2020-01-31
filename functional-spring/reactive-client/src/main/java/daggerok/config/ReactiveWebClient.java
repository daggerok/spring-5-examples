package daggerok.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReactiveWebClient {

  final WebClient webClient;

  public void tryUseRestApi() {

    webClient.get()
             .uri("/")
             .exchange()
             .flatMap(clientResponse -> clientResponse.bodyToMono(String.class))
             .subscribe(log::info);
  }
}
