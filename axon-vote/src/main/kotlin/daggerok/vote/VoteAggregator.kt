package daggerok.vote

import org.axonframework.commandhandling.CommandHandler
import org.axonframework.commandhandling.model.AggregateIdentifier
import org.axonframework.commandhandling.model.AggregateLifecycle.apply
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.spring.stereotype.Aggregate
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import kotlin.math.log

object Logger {
  val loger = LoggerFactory.getLogger("Aggregators")
}

@Component("axonVoteAggregator")
@Aggregate(repository = "axonVoteRepository")
class VoteAggregator() {

  @AggregateIdentifier
  private lateinit var candidateId: String
  private lateinit var name: String
  private var approved: Boolean? = null
  private lateinit var reason: String
  private var votes: MutableMap<String, Set<String>> = mutableMapOf()

  /* candidate registration */
  @CommandHandler
  constructor(cmd: RegisterCandidateCommand) : this() {
    apply(CandidateRegisteredEvent(
        candidateId = cmd.candidateId,
        name = cmd.name
    ))
  }

  @EventSourcingHandler
  fun ob(event: CandidateRegisteredEvent) {
    candidateId = event.candidateId!!
    name = event.name!!
  }

  /* approve candidate registration */
  @CommandHandler
  fun handle(cmd: ApproveRegistrationCommand) {
    apply(RegistrationApprovedEvent(
        candidateId = cmd.candidateId
    ))
  }

  @EventSourcingHandler
  fun on(event: RegistrationApprovedEvent) {
    approved = true
  }

  /* decline candidate registration */
  @CommandHandler
  fun handle(cmd: DeclineRegistrationCommand) {
    apply(RegistrationDeclinedEvent(
        candidateId = cmd.candidateId,
        reason = cmd.reason
    ))
  }

  @EventSourcingHandler
  fun on(event: RegistrationDeclinedEvent) {
    approved = false
    reason = event.reason!!
  }

  /* voting */
  @CommandHandler
  fun handle(cmd: VoteForCandidateCommand) {
    if (approved == false) throw CandidateNotApprovedException()
    apply(VotedForCandidateEvent(
        candidateId = cmd.candidateId,
        elector = cmd.elector
    ))
  }

  /* voting */
  @EventSourcingHandler
  fun on(event: VotedForCandidateEvent) {

    Logger.loger.info("handling {}", event)

    val candidateId = event.candidateId ?: return

    if (true != votes.containsKey(candidateId)) {
      votes[candidateId] = setOf()
    }

    if (votes.filterValues { it.contains(event.elector) }
            .isNotEmpty()) throw ElectorAlreadyVotedException()

    val electorate = votes[candidateId]!!.plus(event.elector!!)

    votes[candidateId] = electorate
  }
}
