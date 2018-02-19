package com.github.rickardoberg.stuff.rest.inbox;

import java.io.IOException;
import java.io.Writer;

import com.github.rickardoberg.cqrs.domain.repository.Repository;
import com.github.rickardoberg.stuff.usecase.Inbox;
import com.github.rickardoberg.stuff.view.InboxModel;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.WriterRepresentation;

public class InboxResource extends Restlet
{
    private final VelocityEngine velocity;
    private Repository repository;
    private TaskResource taskResource;
    private InboxModel model;

    public InboxResource( VelocityEngine velocity, Repository repository, TaskResource
            taskResource, InboxModel model)
    {
        this.velocity = velocity;
        this.repository = repository;
        this.taskResource = taskResource;
        this.model = model;
    }

    @Override
    public void handle( Request request, Response response )
    {
        super.handle( request, response );

        if (request.getAttributes().containsKey( "task" ))
        {
            if (!request.getMethod().isSafe())
            {
                Inbox inbox = new Inbox( );
                request.getAttributes().put( "inbox", inbox );
            }

            taskResource.handle( request, response );
        } else
        {
            if (request.getMethod().isSafe())
            {
                if (request.getAttributes().containsKey( "command" ))
                {
                    response.setEntity( new WriterRepresentation( MediaType.TEXT_HTML )
                    {
                        @Override
                        public void write( Writer writer ) throws IOException
                        {
                            VelocityContext context = new VelocityContext();
                            velocity.getTemplate( "inbox/"+request.getAttributes().get( "command" )+".html" ).merge( context, writer );
                        }
                    } );
                } else
                {
                    response.setEntity( new WriterRepresentation( MediaType.TEXT_HTML )
                    {
                        @Override
                        public void write( Writer writer ) throws IOException
                        {
                            VelocityContext context = new VelocityContext();
                            context.put( "tasks", model.getTasks().entrySet() );
                            velocity.getTemplate( "inbox/inbox.html" ).merge( context, writer );
                        }
                    } );
                }
            } else
            {
                Inbox inbox = new Inbox(  );

                Form form = new Form(request.getEntity());

                switch (request.getAttributes().get( "command" ).toString())
                {
                    case "newtask":
                    {
                        repository.create().apply( "task" ).apply(
                                id ->
                                {
                                    Inbox.ChangeDescription changeDescription = new Inbox.ChangeDescription(  );
                                    changeDescription.description = form.getFirstValue( "description" );
                                    Inbox.NewTask newTask = new Inbox.NewTask( );
                                    newTask.id = id;
                                    newTask.changeDescription = changeDescription;

                                    return Inbox.newTask().apply( inbox ).apply( newTask );
                                });

                        break;
                    }

                    default:
                    {
                        response.setStatus( Status.CLIENT_ERROR_BAD_REQUEST, "Unknown command:"+form.getFirstValue( "command" ) );
                        return;
                    }
                }

                response.redirectSeeOther( request.getOriginalRef().getParentRef() );
            }
        }
    }
}
