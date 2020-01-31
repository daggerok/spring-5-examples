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

val helloApi = ok().body(mapOf("hello" to "world").toMono())

val defaultFallback: (ServerRequest) -> Mono<ServerResponse> = {
  val url = it.uri().toURL()
  val baseURL = "${url.protocol}://${url.authority}"
  ok().body(mapOf(
      "_links" to listOf(
          mapOf(
              "rel" to "hello.v1",
              "href" to "$baseURL/api/v1/hello",
              "templated" to false
          ),
          mapOf(
              "rel" to "hello.v2",
              "href" to "$baseURL/api/v2/hello",
              "templated" to false
          ),
          mapOf(
              "rel" to "index",
              "href" to "$baseURL/",
              "templated" to false
          ),
          mapOf(
              "rel" to "_self",
              "href" to url.toString(),
              "templated" to false
          )
      )
  ).toMono())
}

val v1: RouterFunctionDsl.() -> Unit = {
  GET("/hello") { helloApi }
  GET("/**", defaultFallback)
}

val v2: RouterFunctionDsl.() -> Unit = {
  GET("/2hello") { helloApi }
  GET("/2**", defaultFallback)
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
    GET("/**", defaultFallback)
  }
}

@SpringBootApplication
class AwesomeKotlinApplication

fun main(args: Array<String>) {
  runApplication<AwesomeKotlinApplication>(*args)
}
