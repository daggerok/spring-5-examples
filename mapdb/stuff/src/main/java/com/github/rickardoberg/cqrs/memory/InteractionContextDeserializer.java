package com.github.rickardoberg.cqrs.memory;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.rickardoberg.cqrs.domain.Identifier;
import com.github.rickardoberg.cqrs.event.Event;
import com.github.rickardoberg.cqrs.event.Interaction;
import com.github.rickardoberg.cqrs.event.InteractionContext;

public class InteractionContextDeserializer
    extends StdDeserializer<InteractionContext> {
  public InteractionContextDeserializer() {
    super(InteractionContext.class);
  }

  @Override
  public InteractionContext deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    String type = null;
    long version = 0;
    Date timestamp = null;
    Map<String, String> attributes = new HashMap<>();
    long id = -1;
    List<Event> events = null;
    JsonToken current;
    while ((current = jp.nextToken()) != null) {
      switch (current) {
        case FIELD_NAME: {
          switch (jp.getText()) {
            case "type": {
              jp.nextToken();
              type = jp.getValueAsString();
              break;
            }

            case "version": {
              jp.nextToken();
              version = jp.getValueAsLong();
              break;
            }

            case "timestamp": {
              jp.nextToken();
              timestamp = new Date(jp.getValueAsLong());
              break;
            }

            case "attributes": {
              jp.nextToken();
              attributes = (Map<String, String>) ctxt.findRootValueDeserializer(ctxt.constructType(Map.class)).deserialize(jp, ctxt);
              break;
            }

            case "id": {
              jp.nextToken();
              id = jp.getValueAsLong();
              break;
            }

            case "events": {
              jp.nextToken();
              events = (List<Event>) ctxt.findRootValueDeserializer(ctxt.constructType(List.class).narrowContentsBy(Event.class)).deserialize(jp, ctxt);
              System.out.println(events);
            }
          }
        }
      }
    }

    InteractionContext interactionContext = new InteractionContext(type, version, timestamp, attributes, new Interaction(new Identifier(id), events));

    return interactionContext;
  }
}
