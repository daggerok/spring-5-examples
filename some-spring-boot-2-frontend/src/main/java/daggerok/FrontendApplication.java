package daggerok;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.result.view.Rendering;

//@EnableWebFlux
@SpringBootApplication
public class FrontendApplication implements WebFluxConfigurer {

  @Override
  public void addCorsMappings(final CorsRegistry registry) {
    registry.addMapping("/api/**")
            .allowedOrigins("*")
            .allowedMethods("POST", "PUT", "DELETE")
            .allowCredentials(true)
            .maxAge(3600);
  }

  @Controller
  public static class IndexPage {

    @GetMapping({ "/", "/404" })
    public Rendering index() {
      return Rendering.view("index").build();
    }
/*
    @GetMapping({ "/", "/404" })
    public Mono<String> index() {
      return Mono.just("index");
    }
*/
/*
    @GetMapping({ "/", "/404" })
    public String index() {
      return "index";
    }
*/
  }

  public static void main(String[] args) {
    SpringApplication.run(FrontendApplication.class, args);
  }
}
