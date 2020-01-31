package daggerok;

import daggerok.github.GithubProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(GithubProperties.class)
public class WebfluxGatewayApplication {

  public static void main(String[] args) {
    SpringApplication.run(WebfluxGatewayApplication.class, args);
  }
}
