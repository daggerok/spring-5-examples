package daggerok.vote

import org.axonframework.commandhandling.TargetAggregateIdentifier

data class RegisterCandidateCommand(val candidateId: String, val name: String)
data class CandidateRegisteredEvent(val candidateId: String? = null, val name: String? = null)

data class ApproveRegistrationCommand(@TargetAggregateIdentifier val candidateId: String)
data class RegistrationApprovedEvent(val candidateId: String? = null)

data class DeclineRegistrationCommand(@TargetAggregateIdentifier val candidateId: String, val reason: String)
data class RegistrationDeclinedEvent(val candidateId: String? = null, val reason: String? = null)

data class VoteForCandidateCommand(@TargetAggregateIdentifier val candidateId: String, val elector: String)
data class VotedForCandidateEvent(val candidateId: String? = null, val elector: String? = null)

class CandidateNotApprovedException : RuntimeException()
class ElectorAlreadyVotedException : RuntimeException()
