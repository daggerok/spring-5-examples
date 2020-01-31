package daggerok.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.HashMap;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Controller
public class SpringPage {

  @GetMapping("/")
  public String index(final Model model) {
    System.out.println("model = " + model);
    return "index";
  }

  @GetMapping("/welcome")
  public ModelAndView welcome() {
    String message = "<br><div style='text-align:center;'>"
        + "<h3>Hello World, Spring MVC Tutorial</h3>This message is coming from SpringPage.java </div><br><br>";
    return new ModelAndView("welcome", "message", message);
  }

  @Data
  @NoArgsConstructor
  @Accessors(chain = true)
  @AllArgsConstructor(staticName = "of")
  public static class HealthStatus implements Serializable {
    private String status;
  }

  @SneakyThrows
  @ResponseBody
  @GetMapping("/api/health")
  @RequestMapping(produces = APPLICATION_JSON)
  public HealthStatus index() {
    return HealthStatus.of("UP");
  }
}
