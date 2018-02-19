/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package com.github.rickardoberg.cqrs.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

public class Identifier
{
    private long identifier;

    @JsonCreator
    public Identifier( long identifier )
    {
        this.identifier = identifier;
    }

    public long getIdentifier()
    {
        return identifier;
    }

    public void setIdentifier( long identifier )
    {
        this.identifier = identifier;
    }

    @Override
    public String toString()
    {
        return Long.toString( identifier );
    }

    @Override
    public boolean equals( Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( o == null || getClass() != o.getClass() )
        {
            return false;
        }

        Identifier that = (Identifier) o;

        if ( identifier != that.identifier )
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        return (int) (identifier ^ (identifier >>> 32));
    }
}
