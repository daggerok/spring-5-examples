package daggerok

import org.axonframework.commandhandling.CommandHandler
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.bodyToMono
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.toMono
import java.util.*

@SpringBootApplication
class App(val commandGateway: CommandGateway) {

  @CommandHandler
  fun handle(map: HashMap<String, Any>) = println(map)

  @Bean
  fun routes() = router {
    ("/").nest {
      contentType(APPLICATION_JSON_UTF8)
      POST("/api/voting") {
        val uri = it.uri()
        val url = "${uri.scheme}://${uri.rawAuthority}/api/voting"
        ok().body(
            it.bodyToMono<MutableMap<String, String>>()
                .map { it["subject"].orEmpty() }
                .map {
                  val uuid = UUID.randomUUID()
                  commandGateway.send<HashMap<String, Any>>(hashMapOf(
                      "id" to uuid,
                      "subject" to it
                  ))
                  mapOf(
                      "statistics" to "GET $url/$uuid",
                      "vote" to "POST $url/$uuid"
                  )
                }
        )
      }
      GET("/api/**") {
        ok().body(
            mapOf("hello" to "world").toMono()
        )
      }
    }
  }
}

fun main(args: Array<String>) {
  runApplication<App>(*args)
}
