package daggerok.guest;

import daggerok.api.ActivateGuestCommand;
import daggerok.api.CreateGuestCommand;
import daggerok.api.GuestActivatedEvent;
import daggerok.api.GuestCreatedEvent;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

public class GuestTest {

  FixtureConfiguration<Guest> fixture;

  @Before
  public void setUp() throws Exception {
    fixture = new AggregateTestFixture(Guest.class);
  }

  @Test
  public void create_guest() {

    final LocalDateTime expireAt = LocalDateTime.now().plusDays(1);

    fixture.givenNoPriorActivity()
           .when(new CreateGuestCommand("daggerok", "Max", expireAt))
           .expectEvents(new GuestCreatedEvent("daggerok", "Max", expireAt));
  }

  @Test
  public void activate_guest() {

    fixture.given(new GuestCreatedEvent("daggerok", "max", LocalDateTime.now().plusDays(1)))
           .when(new ActivateGuestCommand("daggerok"))
           .expectEvents(new GuestActivatedEvent("daggerok"));
  }
}
