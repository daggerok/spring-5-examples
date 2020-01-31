package daggerok.entrance;

import daggerok.api.*;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;

import static java.lang.String.format;
import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

@NoArgsConstructor
public class Entrance {

  @AggregateIdentifier
  String entranceId;
  boolean registered;
  boolean closed;

  /* register */

  @CommandHandler
  public Entrance(final RegisterEntranceCommand cmd) {
    apply(new EntranceRegisteredEvent(cmd.getEntranceId()));
  }

  @EventSourcingHandler
  public void on(final EntranceRegisteredEvent event) {
    this.entranceId = event.getEntranceId();
    this.registered = true;
  }

  /* unlock */

  @CommandHandler
  public void handle(final UnlockEntranceCommand cmd) {
    if (!registered)
      throw new IllegalStateException(format("entrance %s not registered.", cmd.getEntranceId()));
    apply(new EntranceUnlockedEvent(cmd.getEntranceId()));
  }

  @EventSourcingHandler
  public void on(final EntranceUnlockedEvent event) {
    closed = false;
  }

  /* lock */

  @CommandHandler
  public void handle(final LockEntranceCommand cmd) {
    apply(new EntranceLockedEvent(cmd.getEntranceId()));
  }

  @EventSourcingHandler
  public void on(final EntranceLockedEvent event) {
    closed = true;
  }

  /* unregister */

  @CommandHandler
  public void handle(final UnregisterEntranceCommand cmd) {
    if (!registered)
      throw new IllegalStateException(format("entrance %s not registered.", cmd.getEntranceId()));
    apply(new EntranceUnregisteredEvent(cmd.getEntranceId()));
  }

  @EventSourcingHandler
  public void on(final EntranceUnregisteredEvent event) {
    registered = false;
  }
}
