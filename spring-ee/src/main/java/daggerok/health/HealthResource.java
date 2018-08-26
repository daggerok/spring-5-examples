package daggerok.health;

import io.vavr.collection.HashMap;

import javax.ejb.Stateless;
import javax.json.Json;
import javax.validation.constraints.Size;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Map;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Stateless
@Path("api")
@Produces(APPLICATION_JSON)
public class HealthResource {

  static final Map<String, String> pingPongMap
    = HashMap.of("ping", "pong",
                 "pong", "ping")
             .toJavaMap();

  @Context
  UriInfo uriInfo;

  @GET
  @Path("")
  public Response index() {
    return Response.ok(Json.createObjectBuilder()
                           .add("health", linkTo("health", "health"))
                           .add("ping", linkTo("health", "ping"))
                           .add("pong", linkTo("health", "pong"))
                           .build())
                   .build();
  }

  String linkTo(@Size(min = 1) final String... args) {
    return uriInfo.getBaseUriBuilder()
                  .path(HealthResource.class)
                  .path(HealthResource.class, args[0])
                  .build(args[1])
                  .toString();
  }

  @GET
  @Path("{path: (health|ping|pong)}")
  public Response health(@PathParam("path") final String path) {
    return Response.ok(Json.createObjectBuilder()
                           .add("status", pingPongMap.getOrDefault(path, "UP"))
                           .build())
                   .build();
  }
}
