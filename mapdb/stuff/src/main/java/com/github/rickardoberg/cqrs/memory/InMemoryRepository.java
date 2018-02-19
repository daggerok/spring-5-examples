package com.github.rickardoberg.cqrs.memory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Block;
import java.util.function.Function;

import com.github.rickardoberg.cqrs.domain.Entity;
import com.github.rickardoberg.cqrs.domain.Identifier;
import com.github.rickardoberg.cqrs.domain.repository.Repository;
import com.github.rickardoberg.cqrs.event.Interaction;
import com.github.rickardoberg.cqrs.event.InteractionContext;
import com.github.rickardoberg.cqrs.event.store.EventSource;
import com.github.rickardoberg.cqrs.event.store.EventStore;

/**
 * In-memory implementation of Repository
 */
public class InMemoryRepository
    implements Repository
{
    private static class RepositoryEntity<T extends Entity>
    {
        private long version;
        private T entity;

        public RepositoryEntity( long version, T entity )
        {
            this.version = version;
            this.entity = entity;
        }

        public long getVersion()
        {
            return version;
        }

        public T getEntity()
        {
            return entity;
        }

        public void apply(InteractionContext interactionContext)
        {
            entity.apply( interactionContext );
            version = interactionContext.getVersion();
        }
    }

    private EventStore eventStore;
    private EventSource eventSource;
    private Function<String, Function<Identifier, ? extends Entity>> entityFactory;

    private Map<String, Map<Identifier, RepositoryEntity<?>>> entities = new HashMap<>(  );

    private long nextId = System.currentTimeMillis();

    public InMemoryRepository( EventStore eventStore, EventSource eventSource, Function<String, Function<Identifier, ? extends Entity>> entityFactory)
    {
        this.eventStore = eventStore;
        this.eventSource = eventSource;
        this.entityFactory = entityFactory;
    }

    @Override
    public <T extends Entity> Function<String, Function<Function<Identifier, T>, InteractionContext>> create( )
    {
        return type -> creator ->
                {
                    Map<Identifier, RepositoryEntity<?>> entityType = entities.get( type );
                    if (entityType == null)
                    {
                        entityType = new HashMap<>(  );
                        entities.put( type, entityType );
                    }

                    T entity = creator.apply( new Identifier( nextId++ ) );

                    entityType.put( entity.getIdentifier(), new RepositoryEntity<T>(0, entity) );

                    InteractionContext interactionContext = new InteractionContext( type, 0, new Date(), new HashMap<>(), entity.getInteraction() );

                    eventStore.add( interactionContext );

                    return interactionContext;
                };
    }

    @Override
    public <T extends Entity> Function<String, Function<Identifier, Function<Block<T>, InteractionContext>>> update()
            throws IllegalArgumentException, IllegalStateException
    {
        return type -> id -> block ->
                {
                    Map<Identifier, RepositoryEntity<?>> entityType = getEntitiesByType(type);

                    RepositoryEntity<T> repositoryEntity = (RepositoryEntity<T>) entityType.get( id );

                    if (repositoryEntity == null)
                    {
                        repositoryEntity = loadRepositoryEntity(type, id);
                        entityType.put( id, repositoryEntity );
                    }

                    // Write lock entity
                    synchronized ( repositoryEntity )
                    {
                        block.accept( repositoryEntity.getEntity() );

                        Interaction interaction = repositoryEntity.getEntity().getInteraction();

                        long newVersion = repositoryEntity.getVersion() + 1;
                        InteractionContext interactionContext = new InteractionContext( type, newVersion, new Date(), new HashMap<>(  ), interaction);

                        if (!interactionContext.getInteraction().getEvents().isEmpty())
                        {
                            eventStore.add( interactionContext );

                            if (repositoryEntity.getEntity().isDeleted())
                                entityType.remove( id );
                            else
                                entityType.put( id, new RepositoryEntity<>(newVersion, repositoryEntity.getEntity()) );
                        }
                        return interactionContext;
                    }
                };
    }

    private Map<Identifier, RepositoryEntity<?>> getEntitiesByType( String type )
    {
        Map<Identifier, RepositoryEntity<?>> entityType = entities.<Map<Identifier, RepositoryEntity>>get( type );
        if (entityType == null)
        {
            entityType = new HashMap<>(  );
            entities.put( type, entityType );
        }
        return entityType;
    }

    private <T extends Entity> RepositoryEntity<T> loadRepositoryEntity( String type, Identifier id )
    {
        RepositoryEntity<T> repositoryEntity;
        repositoryEntity = new RepositoryEntity<T>( -1, (T) entityFactory.apply( type ).apply( id ));
        eventSource.getInteractionsById ().apply( id ).
                orElseThrow( () -> new IllegalArgumentException( "Entity not found:" + id ) ).
                forEach( repositoryEntity::apply );

        if (repositoryEntity.getEntity().isDeleted())
            throw new IllegalArgumentException( "Entity not found:"+ id );
        return repositoryEntity;
    }
}
