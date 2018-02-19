package com.github.rickardoberg.cqrs.memory;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.rickardoberg.cqrs.event.Event;
import com.github.rickardoberg.cqrs.event.InteractionContext;

public class InteractionContextSerializer
    extends StdSerializer<InteractionContext>
{
    public InteractionContextSerializer()
    {
        super( InteractionContext.class );
    }

    @Override
    public void serialize( InteractionContext value, JsonGenerator jgen, SerializerProvider provider ) throws IOException, JsonGenerationException
    {
        jgen.writeStartObject();
        jgen.writeFieldName( "type" );
        jgen.writeString( value.getType() );
        jgen.writeFieldName( "version" );
        jgen.writeNumber( value.getVersion() );
        jgen.writeFieldName( "timestamp" );
        jgen.writeNumber( value.getTimestamp().getTime() );
        jgen.writeFieldName( "id" );
        jgen.writeNumber( value.getInteraction().getIdentifier().getIdentifier() );
        provider.defaultSerializeField( "attributes", value.getAttributes(), jgen );

        jgen.writeFieldName( "events" );
        provider.findValueSerializer( provider.constructType( List.class ).narrowContentsBy( Event.class ), null ).serialize( value.getInteraction().getEvents(), jgen, provider );

        jgen.writeEndObject();

    }
}
