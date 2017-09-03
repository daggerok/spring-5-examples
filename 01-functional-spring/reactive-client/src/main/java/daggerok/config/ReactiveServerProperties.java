package daggerok.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "reactive-server")
public class ReactiveServerProperties {

  /**
   * can be http or https
   */
  String protocol;

  /**
   * rest api server hostname or ip-address, for example: 127.0.0.1
   */
  String host;

  /**
   * rest api server port, for example 3000
   */
  Integer port;
}
