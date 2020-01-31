package daggerok.rest

import com.mongodb.MongoClient
import org.axonframework.eventsourcing.eventstore.EventStore
import org.springframework.beans.factory.annotation.Value
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME
import org.springframework.http.HttpStatus.OK
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE
import org.springframework.web.bind.annotation.*
import java.util.stream.Collectors
import java.util.stream.Collectors.toList
import java.util.stream.StreamSupport

@RestController
@RequestMapping(produces = [APPLICATION_JSON_UTF8_VALUE])
class AxonResource(val eventStore: EventStore,
                   val mongoClient: MongoClient,
                   @Value("\${spring.datasource.name}") val name: String) {

  @GetMapping("/{aggregateId}")
  fun queryEvents(@PathVariable aggregateId: String) =
      eventStore
          .readEvents(aggregateId)
          .asStream()
          .collect(Collectors.toList())

  @ResponseStatus(OK)
  @DateTimeFormat(iso = DATE_TIME)
  @GetMapping(path = ["", "/"])
  fun index(@RequestParam(name = "collection", defaultValue = "snapshots") collection: String) =
      StreamSupport.stream(
          mongoClient
              .getDatabase(name)
              .getCollection(collection)
              .find().map {
            mapOf(
                "_id" to it["_id"],
                "aggregateIdentifier" to it["aggregateIdentifier"],
                "eventIdentifier" to it["eventIdentifier"],
                "timestamp" to it["timestamp"],
                "sequenceNumber" to it["sequenceNumber"],
                "type" to it["type"],
                "payloadType" to it["payloadType"],
                "serializedPayload" to it["serializedPayload"]
            )
          }
              .spliterator(),
          true
      ).collect(toList())
}
