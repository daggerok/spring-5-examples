package daggerok.vote

import daggerok.RestHelper
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.servlet.http.HttpServletRequest

fun <T> CommandGateway.accept(cmd: T) = this.send<T>(cmd)

@RestController
@RequestMapping(
    path = ["/api/v1/registration"],
    produces = [APPLICATION_JSON_UTF8_VALUE])
class RegistrationResources(val commandGateway: CommandGateway) {

  @PostMapping
  fun registerCandidate(request: HttpServletRequest,
                        @RequestBody body: Map<String, String>): ResponseEntity<Any> {

    val candidateId = body["id"] ?: UUID.randomUUID().toString()
    val name = body["name"] ?: return ResponseEntity.badRequest().body(mapOf("error" to "name is required."))

    commandGateway.accept(RegisterCandidateCommand(candidateId = candidateId, name = name))

    val uri = RestHelper
        .builder(request)
        .pathSegment("api", "v1", "registration", "approve")
        .path(candidateId)
        .build()
        .toUri()

    return ResponseEntity.created(uri).body(mapOf(
        "message" to "candidate has been registered. Please approve registration.",
        "PUT" to uri
    ))
  }

  @PutMapping("/decline/{candidateId}")
  fun activateCandidate(request: HttpServletRequest,
                        @RequestBody reason: String,
                        @PathVariable candidateId: String): ResponseEntity<Any> {

    commandGateway.accept(DeclineRegistrationCommand(candidateId = candidateId, reason = reason))

    val uri = RestHelper
        .builder(request)
        .pathSegment("api", "v1", "registration")
        .path(candidateId)
        .build()
        .toUri()

    return ResponseEntity.created(uri).body(mapOf(
        "message" to "Registration has been declined. Please try again later.",
        "POST" to uri
    ))
  }

  @PutMapping("/approve/{candidateId}")
  fun activateCandidate(request: HttpServletRequest,
                        @PathVariable candidateId: String): ResponseEntity<Any> {

    commandGateway.accept(ApproveRegistrationCommand(candidateId = candidateId))

    val votingUri = RestHelper
        .builder(request)
        .pathSegment("api", "v1", "vote")
        .path(candidateId)
        .build()
        .toUri()

    return ResponseEntity.accepted().body(mapOf(
        "message" to "Registration has been approved. Wait for elections begin and send your campaign URL to your electorate making vote for you!",
        "vote by electorId in request body - POST" to votingUri
    ))
  }
}

@RestController
@RequestMapping(produces = [APPLICATION_JSON_UTF8_VALUE])
class VoteResource(val commandGateway: CommandGateway) {

  @PostMapping("/api/v1/vote/{candidateId}")
  fun vote(request: HttpServletRequest,
           @RequestBody electorId: String,
           @PathVariable candidateId: String) {

    commandGateway.accept(VoteForCandidateCommand(candidateId = candidateId, elector = electorId))
  }
}
