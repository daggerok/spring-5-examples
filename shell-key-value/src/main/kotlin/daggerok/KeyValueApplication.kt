package daggerok

import org.springframework.beans.factory.InitializingBean
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.annotation.Id
import org.springframework.data.keyvalue.annotation.KeySpace
import org.springframework.data.map.repository.config.EnableMapRepositories
import org.springframework.data.repository.CrudRepository
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import java.util.concurrent.atomic.AtomicBoolean
import java.util.stream.Stream

@KeySpace("user")
data class User(@Id var id: String? = null,
                var name: String? = null,
                var username: String? = null)
@Repository
interface UserRepository : CrudRepository<User, String>

@Service
class ConnectionService {

  val connected = AtomicBoolean()

  fun isConnected() = connected.get()
  fun connect() {
    connected.set(true)
  }

  fun disconnect() {
    connected.set(false)
  }
}

@ShellComponent
class ConnectionCommands(val svc: ConnectionService) {

  @ShellMethod("connect to the system `connect --name <your name>`")
  fun connect(name: String) = svc.connect()
}

@SpringBootApplication
@EnableMapRepositories(basePackageClasses = arrayOf(UserRepository::class))
class KeyValueApplication {

  @Bean
  fun initializer(repo: UserRepository) = InitializingBean {
    Stream.of("max", "dag", "daggerok")
        .map { User(null, it.capitalize(), it) }
        .forEach { repo.save(it) }

    repo.findAll().forEach { println(it) }
  }
}

fun main(args: Array<String>) {
  SpringApplication.run(KeyValueApplication::class.java, *args)
}
