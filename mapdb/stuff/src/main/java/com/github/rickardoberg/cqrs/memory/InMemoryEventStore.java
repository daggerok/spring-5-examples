package com.github.rickardoberg.cqrs.memory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import com.github.rickardoberg.cqrs.domain.Identifier;
import com.github.rickardoberg.cqrs.event.InteractionContext;
import com.github.rickardoberg.cqrs.event.InteractionContextSink;
import com.github.rickardoberg.cqrs.event.store.EventSource;
import com.github.rickardoberg.cqrs.event.store.EventStore;

public class InMemoryEventStore
    implements EventStore, EventSource {
  List<InteractionContext> allInteractions = new ArrayList<>();
  Map<Identifier, List<InteractionContext>> byId = new HashMap<>();
  Map<String, List<InteractionContext>> byType = new HashMap<>();
  private ArrayList<InteractionContextSink> sinks = new ArrayList<>();

  @Override
  public void add(InteractionContext interactionContext) {
    allInteractions.add(interactionContext);

    Identifier identifier = interactionContext.getInteraction().getIdentifier();
    List<InteractionContext> identifierInteractions = byId.get(identifier);
    if (identifierInteractions == null) {
      identifierInteractions = new ArrayList<>();
      byId.put(identifier, identifierInteractions);
    }
    identifierInteractions.add(interactionContext);

    String type = interactionContext.getType();
    List<InteractionContext> typeInteractions = byType.get(type);
    if (typeInteractions == null) {
      typeInteractions = new ArrayList<>();
      byType.put(type, typeInteractions);
    }
    typeInteractions.add(interactionContext);

    sinks.forEach(listener -> listener.apply(interactionContext));
  }

  @Override
  public Function<Identifier, Optional<Stream<InteractionContext>>> getInteractionsById() {
    return id ->
    {
      List<InteractionContext> interactionContexts = byId.get(id);
      if (interactionContexts == null)
        return Optional.empty();
      else
        return Optional.of(interactionContexts.stream());
    };
  }

  @Override
  public Function<String, Optional<Stream<InteractionContext>>> getInteractionsByType() {
    return type ->
    {
      List<InteractionContext> interactionContexts = byType.get(type);
      if (interactionContexts == null)
        return Optional.empty();
      else
        return Optional.of(interactionContexts.stream());
    };
  }

  @Override
  public Function<Date, Stream<InteractionContext>> getInteractionsByTimestamp() {
    return date -> allInteractions.stream().filter(interaction -> interaction.getTimestamp().after(date));
  }

  @Override
  public void addInteractionContextSink(InteractionContextSink sink) {
    this.sinks.add(sink);
  }

  @Override
  public void removeInteractionContextSink(InteractionContextSink sink) {
    this.sinks.remove(sink);
  }
}
