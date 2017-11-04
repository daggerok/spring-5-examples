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
    val sessions = ConcurrentHashMap<String, MessageHandler>()

    class ForwardingMessageHandler(val session: WebSocketSession, val sink: FluxSink<WebSocketMessage>): MessageHandler {
      private val sessionId = session.id

      override fun handleMessage(message: Message<*>?) {
        val payload = message as String
        val event = MessageEvent(sessionId = sessionId, body = payload)
        val jsonString = om.writeValueAsString(event)
        val wsMessage = session.textMessage(jsonString)
        sink.next(wsMessage)
      }
    }

    return WebSocketHandler { session ->

      val id = session.id

      val publisher = Flux.create(Consumer<FluxSink<WebSocketMessage>> { sink: FluxSink<WebSocketMessage> ->
        sessions[id] = ForwardingMessageHandler(session, sink)
        messagesChannel().subscribe { sessions[id] }
      }).doFinally {
        messagesChannel().unsubscribe(sessions[id])
        sessions.remove(id)
      }

      session.send(publisher)

//      val publisher = Flux.generate(
//          Consumer<SynchronousSink<MessageEvent>> {sink ->
//            sink.next(MessageEvent("${System.currentTimeMillis()}", "ololo - trololo"))
//          })
//          .map { om.writeValueAsString(it) }
//          .map { session.textMessage(it) }
//          .delayElements(Duration.ofMillis(5000))
//
//      session.send(publisher)
    }
  }

  @Bean
  fun messagesChannel() = PublishSubscribeChannel()


}

data class MessageEvent(val sessionId: String, val body: String)
