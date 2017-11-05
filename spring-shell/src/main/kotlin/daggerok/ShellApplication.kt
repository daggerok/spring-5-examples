package daggerok

import org.springframework.boot.SpringApplication
import org.springframework.boot.ansi.AnsiColor
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.annotation.Id
import org.springframework.data.keyvalue.annotation.KeySpace
import org.springframework.data.keyvalue.core.KeyValueTemplate
import org.springframework.data.keyvalue.core.query.KeyValueQuery
import org.springframework.data.map.repository.config.EnableMapRepositories
import org.springframework.data.repository.CrudRepository
import org.springframework.shell.Availability
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import java.io.PrintStream
import java.util.concurrent.atomic.AtomicBoolean

@KeySpace("user")
data class User(@Id var id: String? = null,
                var name: String? = null,
                var username: String? = null) {
  companion object
}

fun User.Companion.of(name: String) = User(
    null,
    name.capitalize(),
    name.toLowerCase()
)

@Repository
interface UserRepository : CrudRepository<User, String>

fun String.containsIgnoreCase(s: String) = this.toLowerCase().contains(s.toLowerCase())

@Service
class ConnectionService(val repo: UserRepository,
                        val template: KeyValueTemplate) {

  val connected = AtomicBoolean()

  fun total() = repo.count()

  fun connect(username: String) {
    repo.save(User.Companion.of(username))
    connected.set(true)
  }

  fun disconnect(username: String) {

    val query = KeyValueQuery("username == '$username'")
    val users = template.find(query, User::class.java)

    if (users.any()) {
      repo.deleteById(users.first().id!!)
      connected.set(false)
    }
  }

  fun isConnected() = connected.get()
  fun all() = repo.findAll()

  fun find(username: String) = repo.findAll()
      .filterNotNull()
      .filterNot { null == it.name }
      .filter { it.name!!.isNotBlank() }
      .filter { it.name!!.containsIgnoreCase(username) }

  fun first(name: String) = find(name).first()
}

@Service
class Console {

  private fun ansiColor(color: AnsiColor) = "\u001B[" + color.toString() + "m"

  val resetAnsiColor = "\u001B[0m"
  val out: PrintStream = System.out

  fun log(pattern: String, args: Array<Any>? = null) {
    out.print("> ")
    out.print(ansiColor(AnsiColor.GREEN))
    out.printf(pattern, *args!!)
    out.println(resetAnsiColor)
  }
}

@ShellComponent
class ConnectionCommands(val svc: ConnectionService,
                         val console: Console) {

  @ShellMethod("""
          connect to the system
            `connect --username <username>`""")
  fun connect(username: String) {
    svc.connect(username)
    console.log("connected: %s", arrayOf(svc.isConnected()))
    console.log("total: %s", arrayOf(svc.total()))
  }

  fun connectAvailability(): Availability =
      if (!svc.isConnected()) Availability.available()
      else Availability.unavailable("please, connect")

  @ShellMethod("""
          disconnect from the system
            `disconnect --username <username>`""")
  fun disconnect(username: String) {
    svc.disconnect(username)
    console.log("disconnected: %s", arrayOf(svc.isConnected()))
    console.log("total: %s", arrayOf(svc.total()))
  }

  fun disconnectAvailability(): Availability =
      if (svc.isConnected()) Availability.available()
      else Availability.unavailable("you are't connected")

  @ShellMethod("""
          find all system users
            `all`""")
  fun all() {
    svc.all()
    console.log("all system users: %s", arrayOf(svc.all()))
  }

  fun allAvailability() = disconnectAvailability()

  @ShellMethod("""
          find any matches system users (full text search)
            `find --any <text>`""")
  fun find(any: String) {
    svc.find(any)
    console.log("found system users: %s", arrayOf(svc.find(any)))
  }

  fun findAvailability() = disconnectAvailability()
}

@SpringBootApplication
@EnableMapRepositories(basePackageClasses = arrayOf(UserRepository::class))
class ShellApplication /*{

  @Bean
  fun initializer(repo: UserRepository) = InitializingBean {
    Stream.of("max", "dag", "daggerok")
        .map { User.Companion.of(it) }
        .forEach { repo.save(it) }

    repo.findAll().forEach { println(it) }
  }
}*/

fun main(args: Array<String>) {
  SpringApplication.run(ShellApplication::class.java, *args)
}
