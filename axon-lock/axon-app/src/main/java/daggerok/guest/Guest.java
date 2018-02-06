package daggerok.guest;

import daggerok.api.ActivateGuestCommand;
import daggerok.api.CreateGuestCommand;
import daggerok.api.GuestActivatedEvent;
import daggerok.api.GuestCreatedEvent;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;

import java.time.LocalDateTime;

import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

@NoArgsConstructor
public class Guest {

  @AggregateIdentifier
  String guestId;
  String name;
  LocalDateTime expireAt;
  boolean active;

  /* create */

  @CommandHandler
  public Guest(final CreateGuestCommand cmd) {
    apply(new GuestCreatedEvent(cmd.getGuestId(), cmd.getName(), cmd.getExpireAt()));
  }

  @EventSourcingHandler
  public void on(final GuestCreatedEvent event) {
    this.guestId = event.getGuestId();
    this.name = event.getName();
    this.expireAt = event.getExpireAt();
  }

  /* activate */

  @CommandHandler
  public void handle(final ActivateGuestCommand cmd) {
    if (!expireAt.isAfter(now()))
      throw new IllegalStateException(format("guest access %s expired.", cmd.getGiestId()));
    apply(new GuestActivatedEvent(cmd.getGiestId()));
  }

  @EventSourcingHandler
  public void on(final GuestActivatedEvent event) {
    this.active = true;
  }

  /* TODO: in */
  /* TODO: out */
}
