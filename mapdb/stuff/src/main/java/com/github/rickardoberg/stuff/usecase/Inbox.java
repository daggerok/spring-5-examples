package com.github.rickardoberg.stuff.usecase;

import java.util.function.Function;

import com.github.rickardoberg.cqrs.domain.Identifier;
import com.github.rickardoberg.cqrs.event.InteractionSource;
import com.github.rickardoberg.stuff.domain.Task;

public class Inbox {
  private Task task;

  public void select(Task task) {
    this.task = task;
  }

  public static class NewTask {
    public ChangeDescription changeDescription;
    public Identifier id;
  }

  public static Function<Inbox, Function<NewTask, Task>> newTask() {
    return inbox -> newTask ->
    {
      Task task = new Task(newTask.id);
      inbox.select(task);
      changeDescription().apply(inbox).apply(newTask.changeDescription);
      return task;
    };
  }

  public static class ChangeDescription {
    public String description;
  }

  public static Function<Inbox, Function<ChangeDescription, InteractionSource>> changeDescription() {
    return inbox -> changeDescription ->
    {
      inbox.task.changeDescription(changeDescription.description);

      return inbox.task;
    };
  }

  public static class TaskDone {
    public boolean done;
  }

  public static Function<Inbox, Function<TaskDone, InteractionSource>> done() {
    return inbox -> done ->
    {
      inbox.task.setDone(done.done);
      return inbox.task;
    };
  }
}
