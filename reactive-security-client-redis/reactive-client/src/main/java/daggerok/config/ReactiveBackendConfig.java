package daggerok.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "boot-reactive-backend")
public class ReactiveBackendConfig {

  String ip;
  Integer port;
  String baseUrl;
  Credentials credentials;

  @Data
  public static class Credentials {
    String username;
    String password;
  }
}
