package com.github.rickardoberg.stuff.domain;

import com.github.rickardoberg.cqrs.domain.Entity;
import com.github.rickardoberg.cqrs.domain.Identifier;
import com.github.rickardoberg.cqrs.event.Event;
import com.github.rickardoberg.stuff.event.ChangedDescriptionEvent;
import com.github.rickardoberg.stuff.event.CreatedEvent;
import com.github.rickardoberg.stuff.event.DoneEvent;

public class Task
    extends Entity {
  private boolean done = false;

  public Task(Identifier identifier) {
    super(identifier);
    add(new CreatedEvent());
  }

  public void changeDescription(String desc) {
    add(new ChangedDescriptionEvent() {{
      description = desc;
    }});
  }

  public boolean isDone() {
    return done;
  }

  public void setDone(boolean isDone) {
    if (this.done != isDone) {
      this.done = isDone;
      add(new DoneEvent() {{
        this.done = isDone;
      }});
    }
  }

  protected void apply(Event event) {
    if (event instanceof DoneEvent) {
      done = ((DoneEvent) event).done;
    }
  }
}
