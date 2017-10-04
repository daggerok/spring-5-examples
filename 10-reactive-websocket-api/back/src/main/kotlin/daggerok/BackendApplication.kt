package daggerok

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.SynchronousSink
import java.time.Duration
import java.util.function.Consumer

@SpringBootApplication
class BackendApplication

fun main(args: Array<String>) {
    SpringApplication.run(BackendApplication::class.java, *args)
}

@Configuration
class WebSocketConfiguration {

  @Bean
  fun adapter() = WebSocketHandlerAdapter()

  @Bean
  fun mapping(): HandlerMapping {
    val res = SimpleUrlHandlerMapping()
    res.order = 10
    res.urlMap = mapOf("/ws/messages" to handler())
    return res
  }

  @Bean
  fun handler(): WebSocketHandler {
    val om = ObjectMapper()
    return WebSocketHandler { session ->
      val publisher = Flux.generate(
          Consumer<SynchronousSink<MessageEvent>> {sink ->
            sink.next(MessageEvent("${System.currentTimeMillis()}", "ololo - trololo"))
          })
          .map { om.writeValueAsString(it) }
          .map { session.textMessage(it) }
          .delayElements(Duration.ofMillis(5000))

      session.send(publisher)
    }
  }
}

data class MessageEvent(val sessionId: String, val body: String)
