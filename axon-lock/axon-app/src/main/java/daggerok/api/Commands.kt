package daggerok.api

import org.axonframework.commandhandling.TargetAggregateIdentifier
import java.time.LocalDateTime

class RegisterEntranceCommand(val entranceId: String)
class UnlockEntranceCommand(@TargetAggregateIdentifier val entranceId: String)
class LockEntranceCommand(@TargetAggregateIdentifier val entranceId: String)
class UnregisterEntranceCommand(@TargetAggregateIdentifier val entranceId: String)

class CreateGuestCommand(val guestId: String,
                         val name: String,
                         val expireAt: LocalDateTime)

class ActivateGuestCommand(@TargetAggregateIdentifier val giestId: String)
class EnterCommand(@TargetAggregateIdentifier val guestId: String, entranceId: String)
class ExitCommand(@TargetAggregateIdentifier val guestId: String, entranceId: String)
