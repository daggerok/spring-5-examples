package daggerok

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ImportResource
import org.springframework.core.io.ClassPathResource
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Mono

interface MyService {
  fun myMethod(): String
}

class MyServiceImpl : MyService {
  override fun myMethod() = "u-la-la!"
}

interface MyUpperCaseService {
  fun up(input: String): String
}

class MyUpperCaseServiceImpl : MyUpperCaseService {
  override fun up(input: String) = input.toUpperCase()
}

@SpringBootApplication
@ImportResource("classpath:/heal-yeah.xml")
class KotlinXmlApplication(val myService: MyService,
                           val myUpperCaseService: MyUpperCaseService) {

  @Bean fun routes() = router {
    "/".nest {
      contentType(MediaType.APPLICATION_JSON_UTF8)
      GET("/**") {
        val input = myService.myMethod()
        val result = myUpperCaseService.up(input)
        ok().body(Mono.just(result), result.javaClass)
      }
    }
    resources("/**", ClassPathResource("static/"))
  }
}

fun main(args: Array<String>) {
  runApplication<KotlinXmlApplication>(*args)
}
