package daggerok.rest

import daggerok.App
import org.mapdb.DB
import org.mapdb.HTreeMap
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Mono
import java.util.*

@Configuration
@ComponentScan(basePackageClasses = [App::class])
class WebfluxRoutesConfig(@Qualifier("eventStoreDB") val eventStoreDB: DB,
                          @Qualifier("inMemoryMap") val inMemoryMap: HTreeMap<UUID, String>) {

  @Bean
  fun routes() = router {
    ("/").nest {
      contentType(APPLICATION_JSON_UTF8)
      GET("/**") {
        ok().body(Mono.just(inMemoryMap.values))
      }
      POST("/**") {
        ok().body(
            it.bodyToMono(Map::class.java)
                .map { it.getOrDefault("message", "no") as String }
                .filter { it != "no" }
                .map {
                  inMemoryMap.put(UUID.randomUUID(), it)
                  inMemoryMap
                }
                .map {
                  eventStoreDB.commit()
                  it
                }
        )
      }
      //accept(TEXT_HTML).nest {
      //  resources("/**", ClassPathResource("static/"))
      //}
    }
  }
}
