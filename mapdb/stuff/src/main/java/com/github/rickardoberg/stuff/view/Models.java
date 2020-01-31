package com.github.rickardoberg.stuff.view;

import com.github.rickardoberg.cqrs.event.InteractionContext;
import com.github.rickardoberg.cqrs.event.InteractionContextSink;

public class Models
    implements InteractionContextSink {
  private Iterable<InteractionContextSink> models;

  public Models(Iterable<InteractionContextSink> models) {
    this.models = models;
  }

  @Override
  public void apply(InteractionContext interaction) {
    for (InteractionContextSink model : models) {
      model.apply(interaction);
    }
  }
}
