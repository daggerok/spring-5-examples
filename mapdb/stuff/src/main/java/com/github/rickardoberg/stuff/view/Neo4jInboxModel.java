package com.github.rickardoberg.stuff.view;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.github.rickardoberg.cqrs.domain.Identifier;
import com.github.rickardoberg.cqrs.event.Event;
import com.github.rickardoberg.cqrs.event.Interaction;
import com.github.rickardoberg.cqrs.event.InteractionContext;
import com.github.rickardoberg.stuff.event.ChangedDescriptionEvent;
import com.github.rickardoberg.stuff.event.CreatedEvent;
import com.github.rickardoberg.stuff.event.DoneEvent;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

public class Neo4jInboxModel
    extends InboxModel
{
    private GraphDatabaseService graphDb;
    private Date lastAppliedTimestamp;
    private final ExecutionEngine cypher;

    public Neo4jInboxModel( GraphDatabaseService graphDb )
    {
        this.graphDb = graphDb;

        Transaction tx = graphDb.beginTx();

        try
        {
            if (graphDb.getReferenceNode().hasProperty( "lastAppliedTimestamp" ))
            {
                lastAppliedTimestamp = new Date((long) graphDb.getReferenceNode().getProperty( "lastAppliedTimestamp" ));
            } else
            {
                lastAppliedTimestamp = new Date(0);
            }
            tx.success();
        }
        finally
        {
            tx.finish();
        }

        cypher = new ExecutionEngine( graphDb );
    }

    @Override
    public void apply( InteractionContext interactionContext )
    {
        if (interactionContext.getTimestamp().before( lastAppliedTimestamp ))
            return; // Already seen this one

        Interaction interaction = interactionContext.getInteraction();

        Transaction tx = graphDb.beginTx();

        try
        {
            Map<String, Object> params = new HashMap<>(  );
            if (interactionContext.getType().equals( "task" ))
                for ( Event event : interaction.getEvents() )
                {
                    if (event instanceof CreatedEvent )
                    {
                        params.clear();
                        params.put( "id", interaction.getIdentifier().getIdentifier() );
                        params.put( "createdOn", interactionContext.getTimestamp().getTime() );
                        for ( Map<String, Object> row : cypher.execute( "start root=node(0) " +
                                "create task={createdOn:{createdOn},id:{id}}, " +
                                "(root)-[:TASK]->(task) " +
                                "return task", params ) )
                        {
                            graphDb.index().forNodes( "tasks" ).add( (Node) row.get( "task" ), "id", interaction.getIdentifier().getIdentifier() );
                        }
                    }
                    else if (event instanceof ChangedDescriptionEvent)
                    {
                        ChangedDescriptionEvent changedDescriptionEvent = (ChangedDescriptionEvent) event;
                        params.clear();
                        params.put( "id", interaction.getIdentifier().getIdentifier() );
                        params.put( "description", changedDescriptionEvent.description );

                        cypher.execute( "start task=node:tasks(id={id}) " +
                                "set task.description={description}" +
                                "return task", params );
                    }
                    else if (event instanceof DoneEvent)
                    {
                    }
                }

            params.clear();
            params.put( "timestamp", interactionContext.getTimestamp().getTime() );
            cypher.execute( "start root=node(0) set root.lastAppliedTimestamp={timestamp}", params );

            tx.success();

            lastAppliedTimestamp = interactionContext.getTimestamp();
        }
        finally
        {
            tx.finish();
        }
    }

    public Map<Identifier, InboxTask> getTasks()
    {
        ExecutionResult result = cypher.execute( "start root=node(0)" +
                        "match (root)-[:TASK]->(task)" +
                        "return task.id, task.createdOn, task.description " +
                        "order by task.createdOn" );

        Map<Identifier, InboxTask> tasks = new HashMap<>(  );

        for ( Map<String, Object> row : result )
        {
            Identifier id = new Identifier( (long) row.get( "task.id" ) );
            InboxTask inboxTask = new InboxTask()
            {{
                description = row.get( "task.description" ).toString();
                createdOn = new Date((long) row.get( "task.createdOn" ));
            }};
            tasks.put( id, inboxTask );
        }

        return tasks;
    }
}
