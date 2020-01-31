package com.github.rickardoberg.cqrs.event;

import java.util.Collection;
import java.util.List;

import com.github.rickardoberg.cqrs.domain.Identifier;

public class Interaction {
  private Identifier identifier;

  private Collection<Event> events;

  public Interaction(Identifier identifier, List<Event> events) {
    this.identifier = identifier;
    this.events = events;
  }

  public Identifier getIdentifier() {
    return identifier;
  }

  public Collection<Event> getEvents() {
    return events;
  }
}
