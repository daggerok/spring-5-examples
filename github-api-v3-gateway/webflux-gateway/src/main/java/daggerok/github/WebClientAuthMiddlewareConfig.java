package daggerok.github;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Base64Utils;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;

import javax.validation.constraints.Pattern;
import java.nio.charset.StandardCharsets;

import static java.lang.String.format;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Configuration
public class WebClientAuthMiddlewareConfig {

  @Bean
  WebClient githubWebClient(final GithubProperties props) {

    return WebClient.builder()
                    .baseUrl(props.getBaseUrl())
                    .filter((request, next) -> next.exchange(
                        ClientRequest.from(request)
                                     .header(AUTHORIZATION, basicAuthorization(props.getToken()))
                                     .build()))
                    .build();
  }

  private static String basicAuthorization(final String token) {

    final byte[] basicAuthValue = token.getBytes(StandardCharsets.UTF_8);
    final String encoded = Base64Utils.encodeToString(basicAuthValue);

    return format("Basic %s", encoded);
  }
}
