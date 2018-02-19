package daggerok

import org.mapdb.DB
import org.mapdb.DBMaker
import org.mapdb.HTreeMap
import org.mapdb.Serializer
import org.mapdb.serializer.SerializerCompressionWrapper
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.support.beans
import java.io.File
import java.nio.file.Paths
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

@SpringBootApplication
class App

fun main(args: Array<String>) {
  runApplication<App>(*args) {
    addInitializers(
        beans {
          bean<ScheduledExecutorService> {
            Executors.newScheduledThreadPool(2)
          }
          bean(name = "eventStoreDB") {
            val pathToEventStoreDb = "./build/events.db"
            Paths.get(pathToEventStoreDb).parent.toAbsolutePath().toFile().mkdirs()
            DBMaker.fileDB(pathToEventStoreDb)
                .allocateStartSize(2 * 1024 * 1024) // 2 Mb
                .allocateIncrement(1 * 1024 * 1024) // 1 Mb
                .fileChannelEnable()
                .fileMmapEnable()
                .fileMmapEnableIfSupported()
                .fileMmapPreclearDisable()
                .transactionEnable()
                .executorEnable()
                .closeOnJvmShutdown()
                .make()
          }
          bean<HTreeMap<UUID, String>>(name = "inMemoryMap") {
            ref<DB>(name = "eventStoreDB").hashMap("inMemoryMap")
                .keySerializer(Serializer.UUID)
                .valueSerializer(Serializer.STRING)
                .valueSerializer(SerializerCompressionWrapper(Serializer.STRING))
                .counterEnable()
                .hashSeed(7131)
                .createOrOpen()
          }
        }
    )
  }
}
