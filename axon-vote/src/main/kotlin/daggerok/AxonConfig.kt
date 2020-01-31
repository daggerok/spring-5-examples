package daggerok

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY
import com.fasterxml.jackson.annotation.PropertyAccessor.FIELD
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature.*
import com.mongodb.MongoClient
import org.axonframework.eventsourcing.EventCountSnapshotTriggerDefinition
import org.axonframework.eventsourcing.SnapshotTriggerDefinition
import org.axonframework.eventsourcing.Snapshotter
import org.axonframework.mongo.DefaultMongoTemplate
import org.axonframework.mongo.MongoTemplate
import org.axonframework.mongo.eventsourcing.eventstore.MongoEventStorageEngine
import org.axonframework.mongo.eventsourcing.eventstore.documentperevent.DocumentPerEventStorageStrategy
import org.axonframework.serialization.Serializer
import org.axonframework.serialization.json.JacksonSerializer
import org.axonframework.serialization.upcasting.event.NoOpEventUpcaster.INSTANCE
import org.axonframework.spring.eventsourcing.SpringAggregateSnapshotterFactoryBean
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@Configuration
@EnableMongoRepositories
class AxonConfig {

  companion object {
    const val amount = 4
  }

  @Bean
  fun springAggregateSnapshotterFactoryBean() = SpringAggregateSnapshotterFactoryBean()

  @Bean
  fun snapshotTriggerDefinition(snapshotter: Snapshotter): SnapshotTriggerDefinition =
      EventCountSnapshotTriggerDefinition(snapshotter, amount)

  @Bean
  fun objectMapper(): ObjectMapper {
    val objectMapper = ObjectMapper()
    objectMapper.setVisibility(FIELD, ANY)
    objectMapper.enable(FAIL_ON_EMPTY_BEANS)
    objectMapper.disable(
        WRITE_DURATIONS_AS_TIMESTAMPS,
        WRITE_DATE_KEYS_AS_TIMESTAMPS,
        WRITE_DATES_AS_TIMESTAMPS
    )
    return objectMapper
  }

  @Bean
  fun serializer(): Serializer = JacksonSerializer(objectMapper()) // XStreamSerializer()

  @Bean("axonMongoTemplate")
  fun axonMongoTemplate(mongoClient: MongoClient, @Value("\${spring.datasource.name}") name: String): MongoTemplate =
      DefaultMongoTemplate(mongoClient, name)
          .withDomainEventsCollection("events")
          .withSnapshotCollection("snapshots")

  @Bean
  fun eventStorageEngine(serializer: Serializer, @Qualifier("axonMongoTemplate") axonMongoTemplate: MongoTemplate) =
      //MongoEventStorageEngine(serializer, INSTANCE, amount, axonMongoTemplate, DocumentPerCommitStorageStrategy())
      MongoEventStorageEngine(serializer, INSTANCE, amount, axonMongoTemplate, DocumentPerEventStorageStrategy())
}
