package com.github.rickardoberg.stuff.usecase;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Iterator;

import com.github.rickardoberg.cqrs.event.Event;
import com.github.rickardoberg.cqrs.event.Interaction;
import com.github.rickardoberg.stuff.domain.Task;
import com.github.rickardoberg.stuff.event.ChangedDescriptionEvent;
import com.github.rickardoberg.stuff.event.CreatedEvent;
import com.github.rickardoberg.stuff.event.DoneEvent;
import org.junit.Test;

public class InboxTest {
  @Test
  public void givenInboxWhenCreateNewTaskThenNewTaskCreated() {
    // Given
    Inbox inbox = new Inbox();

    // When
    Inbox.ChangeDescription changeDescription = new Inbox.ChangeDescription();
    changeDescription.description = "Description";
    Inbox.NewTask newTask = new Inbox.NewTask();
    newTask.changeDescription = changeDescription;
    Interaction interaction = Inbox.newTask().apply(inbox).apply(newTask).getInteraction();

    // Then
    Iterator<Event> events = interaction.getEvents().iterator();
    CreatedEvent createdTask = (CreatedEvent) events.next();
  }

  @Test
  public void givenInboxWithTaskWhenChangeDescriptionThenDescriptionChanged() {
    // Given
    Inbox inbox = new Inbox();

    Inbox.ChangeDescription changeDescription = new Inbox.ChangeDescription();
    changeDescription.description = "Old description";
    Inbox.NewTask newTask = new Inbox.NewTask();
    newTask.changeDescription = changeDescription;
    Task createdTask = Inbox.newTask().apply(inbox).apply(newTask);
    inbox.select(createdTask);
    createdTask.getInteraction();

    // When
    changeDescription = new Inbox.ChangeDescription();
    changeDescription.description = "New description";
    Interaction interaction = Inbox.changeDescription().apply(inbox).apply(changeDescription).getInteraction();

    // Then
    Iterator<Event> events = interaction.getEvents().iterator();
    ChangedDescriptionEvent changedDescription = (ChangedDescriptionEvent) events.next();
    assertThat(changedDescription.description, equalTo("New description"));
  }

  @Test
  public void givenInboxWithTaskWhenDoneThenTaskIsDone() {
    // Given
    Inbox inbox = new Inbox();

    Inbox.ChangeDescription changeDescription = new Inbox.ChangeDescription();
    changeDescription.description = "Description";
    Inbox.NewTask newTask = new Inbox.NewTask();
    newTask.changeDescription = changeDescription;
    Task createdTask = Inbox.newTask().apply(inbox).apply(newTask);
    createdTask.getInteraction();
    inbox.select(createdTask);

    // When
    Inbox.TaskDone taskDone = new Inbox.TaskDone();
    taskDone.done = true;
    Interaction interaction = Inbox.done().apply(inbox).apply(taskDone).getInteraction();

    // Then
    Iterator<Event> events = interaction.getEvents().iterator();
    DoneEvent done = (DoneEvent) events.next();
    assertThat(done.done, equalTo(true));
  }

  @Test
  public void givenInboxWithDoneTaskWhenDoneThenNoChange() {
    // Given
    Inbox inbox = new Inbox();

    Inbox.ChangeDescription changeDescription = new Inbox.ChangeDescription();
    changeDescription.description = "Description";
    Inbox.NewTask newTask = new Inbox.NewTask();
    newTask.changeDescription = changeDescription;
    Task createdTask = Inbox.newTask().apply(inbox).apply(newTask);
    createdTask.getInteraction();

    inbox.select(createdTask);


    Inbox.TaskDone taskDone = new Inbox.TaskDone();
    taskDone.done = true;
    Inbox.done().apply(inbox).apply(taskDone).getInteraction();

    // When
    Interaction interaction = Inbox.done().apply(inbox).apply(taskDone).getInteraction();

    // Then
    Iterator<Event> events = interaction.getEvents().iterator();
    assertThat(events.hasNext(), equalTo(false));
  }
}
