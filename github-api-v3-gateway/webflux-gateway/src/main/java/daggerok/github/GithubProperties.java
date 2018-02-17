package daggerok.github;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "github")
public class GithubProperties {

  /**
   * github token, see:
   */
  String token;
}
