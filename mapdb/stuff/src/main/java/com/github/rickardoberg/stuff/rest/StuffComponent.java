package com.github.rickardoberg.stuff.rest;

import org.restlet.Component;
import org.restlet.data.Protocol;

public class StuffComponent
    extends Component
{
    public StuffComponent()
    {
        getClients().add( Protocol.FILE );
        getDefaultHost().attach( "/", new StuffApplication() );
    }
}
