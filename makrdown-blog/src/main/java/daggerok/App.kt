package daggerok

import daggerok.app.jdbc.JdbcConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

@SpringBootApplication
@Import(JdbcConfig::class)
class App

fun main(args: Array<String>) {
  runApplication<App>(*args)
}
