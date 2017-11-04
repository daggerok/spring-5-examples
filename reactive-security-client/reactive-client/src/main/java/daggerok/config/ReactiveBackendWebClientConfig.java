package daggerok.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ReactiveBackendWebClientConfig {

  @Bean
  public WebClient webClient(final ReactiveBackendConfig backend) {

    return WebClient.builder()
                    .baseUrl(backend.getBaseUrl())
                    .filter(ExchangeFilterFunctions.basicAuthentication(backend.getCredentials().getUsername(),
                                                                        backend.getCredentials().getPassword()))
                    .build();
  }
}
