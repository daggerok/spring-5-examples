package daggerok.counter

import org.axonframework.commandhandling.TargetAggregateIdentifier

data class CreateCounterCommand(val counterId: String)
data class EnableCounterCommand(@TargetAggregateIdentifier val counterId: String)
data class DisableCounterCommand(@TargetAggregateIdentifier val counterId: String)
data class ResetCounterCommand(@TargetAggregateIdentifier val counterId: String, val to: Int)
data class IncrementCounterCommand(@TargetAggregateIdentifier val counterId: String, val amount: Int)
data class DecrementCounterCommand(@TargetAggregateIdentifier val counterId: String, val amount: Int)

data class CounterCreatedEvent(val counterId: String? = null)
data class CounterEnabledEvent(val counterId: String? = null)
data class CounterDisabledEvent(val counterId: String? = null)
data class CounterResettedEvent(val counterId: String? = null, val to: Int? = null)
data class CounterIncrementedEvent(val counterId: String? = null, val amount: Int? = null)
data class CounterDecrementedEvent(val counterId: String? = null, val amount: Int? = null)

class CounterDisabledException : RuntimeException()
