package com.github.rickardoberg.cqrs.memory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.rickardoberg.cqrs.domain.Identifier;
import com.github.rickardoberg.cqrs.event.Event;
import com.github.rickardoberg.cqrs.event.Interaction;
import com.github.rickardoberg.cqrs.event.InteractionContext;
import com.github.rickardoberg.stuff.event.ChangedDescriptionEvent;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FileEventStorageTest
{

    private ObjectMapper mapper;
    private FileEventStorage eventStorage;

    @Before
    public void setup()
    {
        mapper = new ObjectMapper();
        mapper.registerModule( new SimpleModule( "EventSourcing", new Version( 1, 0, 0, null, "eventsourcing",
                "eventsourcing" ) ).
                addSerializer( InteractionContext.class, new InteractionContextSerializer() ).
                addDeserializer( InteractionContext.class, new InteractionContextDeserializer() ).setMixInAnnotation(
                Event.class, JacksonEvent.class ) );

        eventStorage = new FileEventStorage( mapper );
    }

    @Test
    public void givenInteractionContextWithEventWhenSerializeThenIsSerialized() throws JsonProcessingException
    {
        // Given

        List<Event> events = new ArrayList<>(  );
        events.add( new ChangedDescriptionEvent(){{description = "Hello World";}} );
        events.add( new ChangedDescriptionEvent(){{description = "Hello World2";}} );
        Map<String, String> attributes = new HashMap<>(  );
        attributes.put( "user", "rickard" );
        InteractionContext interaction = new InteractionContext("task", 1, new Date(12345), attributes, new Interaction( new Identifier( 1 ), events ));

        // When
        String json = eventStorage.serialize( interaction );
        System.out.println(json);

    }

    @Test
    public void givenEventStringWhenDeserializeThenInteractionContextDeserialized() throws IOException
    {
        // Given
        String json = "{\"type\":\"task\",\"version\":1,\"timestamp\":12345,\"id\":1," +
                "\"attributes\":{\"user\":\"rickard\"},\"events\":[{\"type\":\"com.github.rickardoberg.stuff.event" +
                ".ChangedDescriptionEvent\",\"description\":\"Hello World\"},{\"type\":\"com.github.rickardoberg" +
                ".stuff.event.ChangedDescriptionEvent\",\"description\":\"Hello World2\"}]}\n";

        // When
        InteractionContext context = eventStorage.deserialize( json );

        // Then
        Assert.assertThat( context, CoreMatchers.notNullValue() );
    }
}
