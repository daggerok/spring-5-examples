package com.github.rickardoberg.stuff.view;

import java.util.Date;
import java.util.Map;

import com.github.rickardoberg.cqrs.domain.Identifier;
import com.github.rickardoberg.cqrs.event.InteractionContextSink;

public abstract class InboxModel
    implements InteractionContextSink {
  public abstract Map<Identifier, InboxTask> getTasks();

  public class InboxTask {
    protected String description;
    protected boolean done;
    protected Date createdOn;

    protected void copy(InboxTask task) {
      this.description = task.description;
      this.done = task.done;
      this.createdOn = task.createdOn;
    }

    public String getDescription() {
      return description;
    }

    public boolean isDone() {
      return done;
    }

    public Date getCreatedOn() {
      return createdOn;
    }
  }
}
