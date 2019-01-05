package com.github.daggerok

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.http.MediaType.*
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.ok
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
import java.time.LocalDateTime

val spa: (ServerRequest) -> Mono<RenderingResponse> = {
  RenderingResponse.create("index")
      .modelAttribute("prefix", "Ololo Trololo!")
      .modelAttribute("suffix", LocalDateTime.now())
      .build()
}

val defaultFallback = ok().body(mapOf("hello" to "world").toMono())

val v1: RouterFunctionDsl.() -> Unit = {
  GET("/hello") { defaultFallback }
  GET("/**") { defaultFallback }
}

val v2: RouterFunctionDsl.() -> Unit = {
  GET("/2hello") { defaultFallback }
  GET("/2**") { defaultFallback }
}

@Configuration
class RouterFunctionConfig {
  @Bean fun routes() = router {
    resources("/**", ClassPathResource("/static/"))

    contentType(TEXT_HTML)
    GET("/", spa)

    contentType(APPLICATION_JSON_UTF8)
    (accept(APPLICATION_JSON).and("/api")).nest {
      "/v1".nest(v1)
      "/v2".nest(v2)
    }
    GET("/**") { defaultFallback }
  }
}

@SpringBootApplication
class AwesomeKotlinApplication

fun main(args: Array<String>) {
  runApplication<AwesomeKotlinApplication>(*args)
}
