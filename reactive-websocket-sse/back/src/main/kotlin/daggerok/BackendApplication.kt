package daggerok

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.channel.PublishSubscribeChannel
import org.springframework.messaging.Message
import org.springframework.messaging.MessageHandler
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink
import reactor.core.publisher.SynchronousSink
import java.time.Duration
import java.util.*
import java.util.concurrent.ConcurrentHashMap
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
          Consumer<SynchronousSink<MessageEvent>> { sink ->
            sink.next(MessageEvent(UUID.randomUUID().toString(), "${System.currentTimeMillis()}"))
          })
          .map { om.writeValueAsString(it) }
          .map { session.textMessage(it) }
          .delayElements(Duration.ofMillis(5000))

      session.send(publisher)
    }
  }

  @Bean
  fun messagesChannel() = PublishSubscribeChannel()


}

data class MessageEvent(val sessionId: String, val body: String)
