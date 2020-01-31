package com.github.rickardoberg.cqrs.memory;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.github.rickardoberg.cqrs.event.Event;

@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonTypeIdResolver(CustomClassIdResolver.class)
public class JacksonEvent
    extends Event {
}

