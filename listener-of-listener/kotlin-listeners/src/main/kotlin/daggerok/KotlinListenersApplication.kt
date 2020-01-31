package daggerok

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.event.*
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Bean
import org.springframework.context.event.EventListener
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.accepted
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

data class Event(var payload: String? = null)

@Component
class MyEventListener {

  @EventListener
  fun onApplicationReady(e: Event) {
    println("\nreceived event:\n${e.payload}\n")
  }

  @EventListener
  fun onStarting(e: ApplicationStartingEvent): Event = Event("app is starting...\n$e")

  @EventListener
  fun onPrepared(e: ApplicationPreparedEvent): Event = Event("app was prepared\n$e")

  @EventListener
  fun onStarted(e: ApplicationStartedEvent): Event = Event("app was started\n$e")

  @EventListener
  fun onFailed(e: ApplicationFailedEvent): Event = Event("app was failed\n$e")

  @EventListener
  fun onApplicationReady(e: ApplicationReadyEvent): Event = Event("app is ready")
}

@SpringBootApplication
class KotlinListenersApplication(val publisher: ApplicationEventPublisher) {

  @Bean
  fun routes(): RouterFunction<ServerResponse> = router {

    ("/").nest {

      contentType(MediaType.APPLICATION_JSON_UTF8)

      GET("/**") {
        val map = mapOf("message" to "to send message do method `POST`")
        ok().body(
            Mono.just(map), map.javaClass
        )
      }

      POST("/**") { req ->
        req.bodyToMono(Map::class.java)
            .map {
              val message = it["message"] as String
              publisher.publishEvent(Event(message))
              message
            }
            .publishOn(Schedulers.elastic())
            .flatMap {
              val map = mapOf("result" to "message '$it' sent.")
              accepted().body(Mono.just(map), map.javaClass)
            }
      }
    }
  }
}

fun main(args: Array<String>) {
  runApplication<KotlinListenersApplication>(*args)
}
