package daggerok.rest

import daggerok.RestHelper
import daggerok.counter.*
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping(
    path = ["/api/v1/counter"],
    produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)])
class CounterResource(val commandGateway: CommandGateway) {

  private fun <T> send(cmd: T) = commandGateway.send<T>(cmd)

  @PostMapping
  fun create(request: HttpServletRequest,
             @RequestParam(required = false, name = "counterId") counterId: Optional<String>): ResponseEntity<Any> {

    val id = counterId.orElse(UUID.randomUUID().toString())
    send(CreateCounterCommand(id))

    val uri = RestHelper.builder(request).pathSegment("/api/v1/counter", id).build().toUri()
    return ResponseEntity.created(uri).build()
  }

  @ResponseStatus(HttpStatus.ACCEPTED)
  @PutMapping("/{counterId}/enable")
  fun enable(@PathVariable counterId: String) {
    send(EnableCounterCommand(counterId))
  }

  @ResponseStatus(HttpStatus.ACCEPTED)
  @PutMapping("/{counterId}/disable")
  fun disable(@PathVariable counterId: String) {
    send(DisableCounterCommand(counterId))
  }

  @ResponseStatus(HttpStatus.ACCEPTED)
  @PutMapping("/{counterId}/reset")
  fun reset(@PathVariable counterId: String,
            @RequestParam(required = false, name = "to") to: Int?) {
    send(ResetCounterCommand(counterId, to ?: 0))
  }

  @ResponseStatus(HttpStatus.ACCEPTED)
  @PostMapping("/{counterId}/increment")
  fun increment(@PathVariable counterId: String,
                @RequestParam(required = false, name = "amount") amount: Int?) {

    send(IncrementCounterCommand(counterId, amount ?: 1))
  }

  @ResponseStatus(HttpStatus.ACCEPTED)
  @PostMapping("/{counterId}/decrement")
  fun decrement(@PathVariable counterId: String,
                @RequestParam(required = false, name = "amount") amount: Int?) {

    send(DecrementCounterCommand(counterId, amount ?: 1))
  }
}
