package daggerok;

import daggerok.api.RegisterEntranceCommand;
import daggerok.api.UnlockEntranceCommand;
import daggerok.api.UnregisterEntranceCommand;
import daggerok.entrance.Entrance;
import daggerok.guest.Guest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.AsynchronousCommandBus;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.config.Configuration;
import org.axonframework.config.DefaultConfigurer;
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine;

import java.util.concurrent.TimeUnit;

import static org.axonframework.commandhandling.GenericCommandMessage.asCommandMessage;

@Slf4j
public class AxonManualApplication {

  @SneakyThrows
  public static void disabledMain(String[] args) {

    final Configuration configuration = DefaultConfigurer.defaultConfiguration()
                                                         .configureAggregate(Entrance.class)
                                                         .configureAggregate(Guest.class)
                                                         .configureEmbeddedEventStore(c -> new InMemoryEventStorageEngine())
                                                         .configureCommandBus(c -> new AsynchronousCommandBus())
                                                         .buildConfiguration();

    configuration.start();

    final CommandBus commandBus = configuration.commandBus();

    commandBus.dispatch(asCommandMessage(new RegisterEntranceCommand("main")));
    commandBus.dispatch(asCommandMessage(new UnlockEntranceCommand("main")));
    commandBus.dispatch(asCommandMessage(new UnregisterEntranceCommand("main")) );

    TimeUnit.SECONDS.sleep(1L);
  }
}
