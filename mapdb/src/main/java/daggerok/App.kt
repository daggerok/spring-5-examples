package daggerok

import org.mapdb.DBMaker
import org.mapdb.HTreeMap
import org.mapdb.Serializer
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.support.beans
import java.util.*


@SpringBootApplication
class App

fun main(args: Array<String>) {
  runApplication<App>(*args) {
    addInitializers(
        beans {
          bean<HTreeMap<UUID, String>>(name = "myMap") {
            DBMaker.memoryDB().make().hashMap("myMap", Serializer.UUID, Serializer.STRING).createOrOpen()
          }
        }
    )
  }
}
