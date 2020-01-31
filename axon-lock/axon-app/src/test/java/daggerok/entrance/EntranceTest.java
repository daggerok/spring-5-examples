package daggerok.entrance;

import daggerok.api.*;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.Before;
import org.junit.Test;

public class EntranceTest {

  FixtureConfiguration<Entrance> fixture;

  @Before
  public void setUp() throws Exception {
    fixture = new AggregateTestFixture<>(Entrance.class);
  }

  /*
    see @AggregateIdentifier in daggerok.entrance.Entrance.entranceId
   */
  @Test
  public void register_entrance() {

    fixture.givenNoPriorActivity()
           .when(new RegisterEntranceCommand("main-door"))
           .expectEvents(new EntranceRegisteredEvent("main-door"));
  }

  /*
    see @TargetAggregateIdentifier in daggerok.api.UnlockEntranceCommand.entranceId
   */
  @Test
  public void unlock_entrance() {

    fixture.given(new EntranceRegisteredEvent("main-door"))
           .when(new UnlockEntranceCommand("main-door"))
           .expectEvents(new EntranceUnlockedEvent("main-door"));
  }

  @Test
  public void unlock_unregistered_entrance() {

    fixture.given(new EntranceRegisteredEvent("main-door"),
                  new EntranceUnregisteredEvent("main-door"))
           .when(new UnlockEntranceCommand("main-door"))
           .expectNoEvents()
           .expectException(IllegalStateException.class);
  }

  @Test
  public void lock_entrance() {

    fixture.given(new EntranceRegisteredEvent("main-door"),
                  new EntranceUnlockedEvent("main-door"))
           .when(new LockEntranceCommand("main-door"))
           .expectEvents(new EntranceLockedEvent("main-door"));
  }

  @Test
  public void unregister_entrance() {

    fixture.given(new EntranceRegisteredEvent("main-door"),
                  new EntranceUnlockedEvent("main-door"))
           .when(new UnregisterEntranceCommand("main-door"))
           .expectEvents(new EntranceUnregisteredEvent("main-door"));
  }
}
