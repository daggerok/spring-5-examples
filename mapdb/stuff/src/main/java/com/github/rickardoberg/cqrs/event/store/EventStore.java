/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package com.github.rickardoberg.cqrs.event.store;

import com.github.rickardoberg.cqrs.event.InteractionContext;

public interface EventStore {
  void add(InteractionContext interactionContext);
}
