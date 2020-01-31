package daggerok;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import static java.util.Collections.singletonMap;

@SpringBootApplication
public class ReactiveBackendApplication {

  public static void main(String[] args) {

    new SpringApplicationBuilder(ReactiveBackendApplication.class)
        .properties(singletonMap("server.port", 8001))
        .run(args);
  }
}
