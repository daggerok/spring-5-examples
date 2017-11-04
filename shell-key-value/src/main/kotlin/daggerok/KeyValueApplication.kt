package daggerok

import org.springframework.boot.ApplicationRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.annotation.Id
import org.springframework.data.keyvalue.annotation.KeySpace
import org.springframework.data.map.repository.config.EnableMapRepositories
import org.springframework.data.repository.CrudRepository
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8
import org.springframework.stereotype.Repository
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Flux
import java.util.stream.Stream

@KeySpace("user")
data class User(@Id var id: String? = null,
                var name: String? = null,
                var username: String? = null)
@Repository
interface UserRepository : CrudRepository<User, String>

@SpringBootApplication
@EnableMapRepositories(basePackageClasses = arrayOf(UserRepository::class))
class KeyValueApplication {

  @Bean
  fun initializer(repo: UserRepository) = ApplicationRunner {
    Stream.of("max", "dag", "daggerok")
          .map { User(null, it.capitalize(), it) }
          .forEach { repo.save(it) }
  }

  @Bean
  fun routes(repo: UserRepository) = router {

    fun getData(@Suppress("UNUSED_PARAMETER") req: ServerRequest) =
        ServerResponse.ok().body(Flux.fromIterable(repo.findAll()))

    (accept(APPLICATION_JSON_UTF8) and "/").nest {
      GET("/**", ::getData)
    }
  }
}

fun main(args: Array<String>) {
  SpringApplication.run(KeyValueApplication::class.java, *args)
}
