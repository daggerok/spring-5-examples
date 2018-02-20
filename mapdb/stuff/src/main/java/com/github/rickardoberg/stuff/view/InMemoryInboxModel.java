package com.github.rickardoberg.stuff.view;

import java.util.LinkedHashMap;
import java.util.Map;

import com.github.rickardoberg.cqrs.domain.Identifier;
import com.github.rickardoberg.cqrs.event.Event;
import com.github.rickardoberg.cqrs.event.Interaction;
import com.github.rickardoberg.cqrs.event.InteractionContext;
import com.github.rickardoberg.stuff.event.ChangedDescriptionEvent;
import com.github.rickardoberg.stuff.event.CreatedEvent;
import com.github.rickardoberg.stuff.event.DoneEvent;

public class InMemoryInboxModel
    extends InboxModel {
  Map<Identifier, InboxTask> tasks = new LinkedHashMap<>();

  @Override
  public void apply(InteractionContext interactionContext) {
    Interaction interaction = interactionContext.getInteraction();

    for (Event event : interaction.getEvents()) {
      if (event instanceof CreatedEvent) {
        InboxTask task = new InboxTask() {{
          createdOn = interactionContext.getTimestamp();
        }};
        tasks.put(interaction.getIdentifier(), task);
      } else if (event instanceof ChangedDescriptionEvent) {
        ChangedDescriptionEvent changedDescriptionEvent = (ChangedDescriptionEvent) event;
        tasks.put(interaction.getIdentifier(), new InboxTask() {{
          copy(tasks.get(interaction.getIdentifier()));
          description = changedDescriptionEvent.description;
        }});
      } else if (event instanceof DoneEvent) {
        DoneEvent doneEvent = (DoneEvent) event;
        tasks.put(interaction.getIdentifier(), new InboxTask() {{
          copy(tasks.get(interaction.getIdentifier()));
          done = doneEvent.done;
        }});
      }
    }
  }

  public Map<Identifier, InboxTask> getTasks() {
    return tasks;
  }
}
