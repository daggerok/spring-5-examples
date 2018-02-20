package com.github.rickardoberg.stuff.view;

import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.github.rickardoberg.cqrs.domain.Identifier;
import com.github.rickardoberg.cqrs.event.Event;
import com.github.rickardoberg.cqrs.event.Interaction;
import com.github.rickardoberg.cqrs.event.InteractionContext;
import com.github.rickardoberg.stuff.event.CreatedEvent;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

public class InboxModelTest {
  @Test
  public void givenEmptyModelWhenCreatedTaskThenModelHasTask() {
    // Given
    InboxModel model = new InMemoryInboxModel();

    // When
    List<Event> events = new ArrayList<>();
    Identifier id = new Identifier(0);
    events.add(new CreatedEvent());
    InteractionContext context = new InteractionContext("task", -1, new Date(), Collections.<String, String>emptyMap(), new Interaction(id, events));
    model.apply(context);

    // Then
    assertThat(model.getTasks().entrySet().size(), CoreMatchers.equalTo(1));
  }
}
