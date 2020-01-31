package daggerok.api

import java.time.LocalDateTime

class EntranceRegisteredEvent(val entranceId: String)
class EntranceUnlockedEvent(val entranceId: String)
class EntranceLockedEvent(val entranceId: String)
class EntranceUnregisteredEvent(val entranceId: String)

class GuestCreatedEvent(val guestId: String,
                        val name: String,
                        val expireAt: LocalDateTime)

class GuestActivatedEvent(val giestId: String)
class GuestEnteredEvent(val guestId: String, entranceId: String)
class GuestExitedEvent(val guestId: String, entranceId: String)
