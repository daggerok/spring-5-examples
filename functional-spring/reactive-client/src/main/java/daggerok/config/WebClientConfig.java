package daggerok.config;

import lombok.val;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import static java.lang.String.format;

@Configuration
public class WebClientConfig {

  @Bean
  public WebClient webClient(final ReactiveServerProperties props) {

    val baseUrl = format("%s://%s:%d", props.getProtocol(), props.getHost(), props.getPort());
    return WebClient.create(baseUrl);
  }

  @Bean
  public CommandLineRunner commandLineRunner(final ReactiveWebClient client) {
    return args -> client.tryUseRestApi();
  }
}
