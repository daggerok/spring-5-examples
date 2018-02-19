/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package com.github.rickardoberg.cqrs.event.store;

import java.util.Date;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import com.github.rickardoberg.cqrs.domain.Identifier;
import com.github.rickardoberg.cqrs.event.InteractionContext;
import com.github.rickardoberg.cqrs.event.InteractionContextSink;

public interface EventSource
{
    Function<Identifier, Optional<Stream<InteractionContext>>> getInteractionsById();
    Function<String, Optional<Stream<InteractionContext>>> getInteractionsByType();
    Function<Date, Stream<InteractionContext>> getInteractionsByTimestamp();

    void addInteractionContextSink( InteractionContextSink sink );
    void removeInteractionContextSink( InteractionContextSink sink );
}
