package com.github.rickardoberg.cqrs.domain;

public class VersionedId
{
    private Identifier id;
    private long version;

    public VersionedId( Identifier id, long version )
    {
        this.id = id;
        this.version = version;
    }

    public Identifier getId()
    {
        return id;
    }

    public long getVersion()
    {
        return version;
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

        VersionedId that = (VersionedId) o;

        if ( version != that.version )
        {
            return false;
        }
        if ( !id.equals( that.id ) )
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = id.hashCode();
        result = 31 * result + (int) (version ^ (version >>> 32));
        return result;
    }

    @Override
    public String toString()
    {
        return id+"#"+version;
    }

    public VersionedId next()
    {
        return new VersionedId(id, version+1);
    }
}
