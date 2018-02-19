package com.github.rickardoberg.stuff.domain;

import com.github.rickardoberg.cqrs.domain.Entity;
import com.github.rickardoberg.cqrs.domain.Identifier;
import com.github.rickardoberg.stuff.event.ChangedDescriptionEvent;
import com.github.rickardoberg.stuff.event.DoneEvent;

public class Project
    extends Entity
{
    private boolean done;

    public Project( Identifier identifier )
    {
        super( identifier );
    }

    public void changeDescription( String desc )
    {
        add( new ChangedDescriptionEvent(){{this.description = desc;}});
    }

    public void setDone(boolean isDone)
    {
        if (this.done != isDone)
        {
            this.done = isDone;
            add( new DoneEvent(){{done = isDone;}} );
        }
    }

    public boolean isDone()
    {
        return done;
    }
}
