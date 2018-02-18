package daggerok.counter

import org.axonframework.eventsourcing.AggregateFactory
import org.axonframework.eventsourcing.EventSourcingRepository
import org.axonframework.eventsourcing.SnapshotTriggerDefinition
import org.axonframework.eventsourcing.eventstore.EventStore
import org.axonframework.spring.eventsourcing.SpringPrototypeAggregateFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AxonCounterConfig {

  @Bean // required by axonCounterRepository
  fun axonCounterAggregatorFactoryBean(): AggregateFactory<CounterAggregator> {
    val prototypeAggregateFactory = SpringPrototypeAggregateFactory<CounterAggregator>()
    prototypeAggregateFactory.setPrototypeBeanName("axonCounterAggregator")
    return prototypeAggregateFactory
  }

  @Bean("axonCounterRepository")
  fun axonCounterRepository(axonCounterAggregatorFactoryBean: AggregateFactory<CounterAggregator>, eventStore: EventStore,
                            snapshotTriggerDefinition: SnapshotTriggerDefinition)//: Repository<CounterAggregator> =
      = EventSourcingRepository<CounterAggregator>(axonCounterAggregatorFactoryBean, eventStore, snapshotTriggerDefinition)
}
