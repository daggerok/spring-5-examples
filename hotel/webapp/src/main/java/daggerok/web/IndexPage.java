package daggerok.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.result.view.Rendering;

import java.util.Optional;

@Controller
public class IndexPage {

  /**
   * GET http://localhost:8080
   * GET http://localhost:8080?name=Max
   */
  @GetMapping({ "/", "/404" })
  public Rendering index(@RequestParam(required = false, name = "name") final Optional<String> name) {

    final String result = name.orElse("World");

    return Rendering.view("index")
                    .modelAttribute("name", result)
                    .build();
  }
}
