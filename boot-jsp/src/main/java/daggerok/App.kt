package daggerok

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
@SpringBootApplication
class App {
  @GetMapping("/") fun index() = "index"
}

fun main(args: Array<String>) {
  runApplication<App>(*args)
}
