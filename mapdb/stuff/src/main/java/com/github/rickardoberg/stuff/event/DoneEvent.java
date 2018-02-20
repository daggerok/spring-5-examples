package com.github.rickardoberg.stuff.event;

import com.github.rickardoberg.cqrs.event.Event;

public class DoneEvent
    extends Event {
  public boolean done;
}
