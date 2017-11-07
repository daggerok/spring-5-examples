package daggerok;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
@SpringBootApplication
public class MustacheJsonApplication {

  @GetMapping("/hello/{name}")
  public String index(@PathVariable final Optional<String> name, final Model model) {
    model.addAttribute("name", "world");
    name.ifPresent(s -> {
      model.addAttribute("name", s);
    });
    return "hello";
  }

  @GetMapping
  public String index() {
    return "index";
  }

  public static void main(String[] args) {
    SpringApplication.run(MustacheJsonApplication.class, args);
  }
}
