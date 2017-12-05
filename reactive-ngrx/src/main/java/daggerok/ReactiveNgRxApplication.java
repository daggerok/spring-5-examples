package daggerok;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class ReactiveNgRxApplication {

  public static void main(String[] args) {
    SpringApplication.run(ReactiveNgRxApplication.class, args);
  }

  @Controller
  public static class HomePage {

    @GetMapping({"", "/"})
    public Mono<String> index() {
      return Mono.just("index");
    }
  }
}
