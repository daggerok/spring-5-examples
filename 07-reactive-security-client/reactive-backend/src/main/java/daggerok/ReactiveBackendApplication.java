package daggerok;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.Collections;

@SpringBootApplication
public class ReactiveBackendApplication {

  public static void main(String[] args) {
    new SpringApplicationBuilder(ReactiveBackendApplication.class)
        .properties(Collections.singletonMap("server.port", 8001))
        .run(args);
  }
}
