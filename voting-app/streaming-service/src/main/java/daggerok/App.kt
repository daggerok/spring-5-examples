package daggerok

import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType.*
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.toMono
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.core.io.ClassPathResource
import reactor.core.publisher.toFlux

@SpringBootApplication
class App {

  @Bean
  fun routes() = router {
    ("/").nest {
      contentType(APPLICATION_STREAM_JSON)
      GET("/json/**") {
        ok().body(
            listOf("hello", "json").toFlux()
        )
      }
      contentType(TEXT_EVENT_STREAM)
      GET("/text/**") {
        ok().body(
            listOf("hello", "text").toFlux()
        )
      }
      contentType(APPLICATION_JSON_UTF8)
      GET("/**") {
        ok().body(
          mapOf("hello" to "world").toMono()
        )
      }
    }
    resources("/**", ClassPathResource("/public"))
  }
}

fun main(args: Array<String>) {
  runApplication<App>(*args)
}
