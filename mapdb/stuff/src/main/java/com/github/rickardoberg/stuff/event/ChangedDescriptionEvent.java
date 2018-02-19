package com.github.rickardoberg.stuff.event;

import com.github.rickardoberg.cqrs.event.Event;

public class ChangedDescriptionEvent
    extends Event
{
    public String description;
}
