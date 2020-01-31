package daggerok.vote

import org.axonframework.commandhandling.model.Repository
import org.axonframework.eventsourcing.AggregateFactory
import org.axonframework.eventsourcing.EventSourcingRepository
import org.axonframework.eventsourcing.SnapshotTriggerDefinition
import org.axonframework.eventsourcing.eventstore.EventStore
import org.axonframework.spring.eventsourcing.SpringPrototypeAggregateFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AxonVoteConfig {

  @Bean
  fun axonVoteAggregatorFactoryBean(): AggregateFactory<VoteAggregator> {
    val prototypeAggregateFactory = SpringPrototypeAggregateFactory<VoteAggregator>()
    prototypeAggregateFactory.setPrototypeBeanName("axonVoteAggregator")
    return prototypeAggregateFactory
  }

  @Bean
  fun axonVoteRepository(axonVoteAggregatorFactoryBean: AggregateFactory<VoteAggregator>, eventStore: EventStore,
                         snapshotTriggerDefinition: SnapshotTriggerDefinition): Repository<VoteAggregator> =
      EventSourcingRepository<VoteAggregator>(
          axonVoteAggregatorFactoryBean,
          eventStore, snapshotTriggerDefinition
      )
}
