package com.github.rickardoberg.stuff.rest;

import java.io.File;
import java.util.Properties;
import java.util.function.Function;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.rickardoberg.cqrs.domain.Entity;
import com.github.rickardoberg.cqrs.domain.Identifier;
import com.github.rickardoberg.cqrs.domain.repository.Repository;
import com.github.rickardoberg.cqrs.event.Event;
import com.github.rickardoberg.cqrs.event.InteractionContext;
import com.github.rickardoberg.cqrs.memory.FileEventStorage;
import com.github.rickardoberg.cqrs.memory.InMemoryEventStore;
import com.github.rickardoberg.cqrs.memory.InMemoryRepository;
import com.github.rickardoberg.cqrs.memory.InteractionContextDeserializer;
import com.github.rickardoberg.cqrs.memory.InteractionContextSerializer;
import com.github.rickardoberg.cqrs.memory.JacksonEvent;
import com.github.rickardoberg.stuff.domain.Project;
import com.github.rickardoberg.stuff.domain.Task;
import com.github.rickardoberg.stuff.rest.inbox.InboxResource;
import com.github.rickardoberg.stuff.rest.inbox.TaskResource;
import com.github.rickardoberg.stuff.view.InboxModel;
import com.github.rickardoberg.stuff.view.LoggingModel;
import com.github.rickardoberg.stuff.view.Neo4jInboxModel;
import org.apache.velocity.app.VelocityEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.restlet.Application;
import org.restlet.data.Reference;
import org.restlet.resource.Directory;
import org.restlet.routing.Router;
import org.restlet.routing.Template;
import org.slf4j.LoggerFactory;

/**
 * To understand how Stuff works, start here. This class binds everything together.
 */
public class StuffApplication
    extends Application
{

    private InMemoryEventStore eventStore;
    private FileEventStorage storage;
    private GraphDatabaseService graphDb;

    @Override
    public synchronized void start() throws Exception
    {
        Router router = new Router(getContext());


        Properties props = new Properties();
        props.load( getClass().getResourceAsStream( "/velocity.properties" ) );

        VelocityEngine velocity = new VelocityEngine( props );

        Function<String, Function<Identifier, ? extends Entity>> entityFactory =
                type -> identifier ->
                        {
                            switch ( type )
                            {
                                case "task":
                                    return new Task(identifier);

                                case "project":
                                    return new Project(identifier);
                            }

                            throw new IllegalArgumentException( type );
                        };

        eventStore = new InMemoryEventStore(  );
        Repository repository = new InMemoryRepository( eventStore, eventStore, entityFactory);

        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( "graphdb" );

        InboxModel inboxModel = new Neo4jInboxModel(graphDb);
        eventStore.addInteractionContextSink( inboxModel );

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule( new SimpleModule("EventSourcing", new Version(1,0,0, null, "eventsourcing", "eventsourcing")).
                addSerializer( InteractionContext.class, new InteractionContextSerializer() ).
                addDeserializer( InteractionContext.class, new InteractionContextDeserializer() ).setMixInAnnotation( Event.class, JacksonEvent.class )
                );

        File file = new File( "events.log" ).getAbsoluteFile();
        LoggerFactory.getLogger( getClass() ).info( "Event log:"+file );
        storage = new FileEventStorage(mapper);

        if (file.exists())
            storage.load( file, eventStore );

        storage.save( file, eventStore );

        eventStore.addInteractionContextSink( new LoggingModel( mapper ) );

        InboxResource inboxResource = new InboxResource( velocity, repository,
                                                         new TaskResource( velocity, repository, inboxModel ), inboxModel );
        router.attach( "inbox/{task}/{command}", inboxResource );
        router.attach( "inbox/{task}/", inboxResource );
        router.attach( "inbox/{command}", inboxResource );
        router.attach( "inbox/", inboxResource );

        Reference staticContent = new Reference( new File(getClass().getResource( "/htdocs/index.html" ).getFile()).getParentFile().toURI() );
        router.attachDefault( new Directory( getContext(), staticContent ) ).setMatchingMode( Template.MODE_STARTS_WITH );

        setInboundRoot( router );

        super.start();
    }

    @Override
    public synchronized void stop() throws Exception
    {
        super.stop();

        storage.close();

        graphDb.shutdown();
    }

}
