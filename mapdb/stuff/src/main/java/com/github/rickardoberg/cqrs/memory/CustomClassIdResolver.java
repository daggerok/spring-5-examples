package com.github.rickardoberg.cqrs.memory;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;
import com.fasterxml.jackson.databind.type.SimpleType;

public class CustomClassIdResolver
    extends TypeIdResolverBase
{
    public CustomClassIdResolver( )
    {
        super( null, null );
    }

    @Override
    public String idFromValue( Object value )
    {
        Class<?> aClass = value.getClass();
        if (aClass.isAnonymousClass())
            return aClass.getSuperclass().getName();
        else
            return aClass.getName();
    }

    @Override
    public String idFromValueAndType( Object value, Class<?> suggestedType )
    {
        return null;
    }

    @Override
    public JavaType typeFromId( String id )
    {
        try
        {
            return SimpleType.construct( Thread.currentThread().getContextClassLoader().loadClass( id ) );
        }
        catch ( ClassNotFoundException e )
        {
            throw new RuntimeException( e );
        }
    }

    @Override
    public JsonTypeInfo.Id getMechanism()
    {
        return JsonTypeInfo.Id.CUSTOM;
    }
}
