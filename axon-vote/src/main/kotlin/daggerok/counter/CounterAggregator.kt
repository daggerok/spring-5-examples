package daggerok.counter

import org.axonframework.commandhandling.CommandHandler
import org.axonframework.commandhandling.model.AggregateIdentifier
import org.axonframework.commandhandling.model.AggregateLifecycle
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.spring.stereotype.Aggregate
import org.springframework.stereotype.Component

@Component("axonCounterAggregator")
@Aggregate(repository = "axonCounterRepository")
class CounterAggregator() {

  @AggregateIdentifier
  lateinit var counterId: String
  var enabled: Boolean = false
  var counter: Int = 0

  @CommandHandler
  constructor(cmd: CreateCounterCommand) : this() {
    AggregateLifecycle.apply(CounterCreatedEvent(cmd.counterId))
  }

  @EventSourcingHandler
  fun on(evt: CounterCreatedEvent) {
    counterId = evt.counterId!!
  }

  @CommandHandler
  fun handle(cmd: EnableCounterCommand) {
    if (!enabled) AggregateLifecycle.apply(CounterEnabledEvent(cmd.counterId))
  }

  @EventSourcingHandler
  fun on(evt: CounterEnabledEvent) {
    enabled = true
  }

  @CommandHandler
  fun handle(cmd: DisableCounterCommand) {
    if (enabled) AggregateLifecycle.apply(CounterDisabledEvent(cmd.counterId))
  }

  @EventSourcingHandler
  fun on(evt: CounterDisabledEvent) {
    enabled = false
  }

  @CommandHandler
  fun handle(cmd: IncrementCounterCommand) {
    if (!enabled) throw CounterDisabledException()
    AggregateLifecycle.apply(CounterIncrementedEvent(cmd.counterId, cmd.amount))
  }

  @EventSourcingHandler
  fun on(evt: CounterIncrementedEvent) {
    counter += evt.amount!!
  }

  @CommandHandler
  fun handle(cmd: DecrementCounterCommand) {
    if (!enabled) throw CounterDisabledException()
    AggregateLifecycle.apply(CounterDecrementedEvent(cmd.counterId, cmd.amount))
  }

  @EventSourcingHandler
  fun on(evt: CounterDecrementedEvent) {
    counter -= evt.amount!!
  }

  @CommandHandler
  fun handle(cmd: ResetCounterCommand) {
    if (!enabled) throw CounterDisabledException()
    AggregateLifecycle.apply(CounterResettedEvent(cmd.counterId))
  }

  @EventSourcingHandler
  fun on(evt: CounterResettedEvent) {
    counter = evt.to!!
  }
}
