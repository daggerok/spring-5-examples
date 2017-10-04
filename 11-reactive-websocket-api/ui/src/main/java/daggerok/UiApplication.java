package daggerok;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Controller
@SpringBootApplication
public class UiApplication {

  @GetMapping({
      "/",
      "/404"
  })
  public String index() {
    return "/index.html";
  }

  @Bean
  public EmbeddedServletContainerCustomizer embeddedServletContainerCustomizer() {
    return container -> container.addErrorPages(
        new ErrorPage(NOT_FOUND, "/404")
    );
  }

  public static void main(String[] args) {
    SpringApplication.run(UiApplication.class, args);
  }
}
