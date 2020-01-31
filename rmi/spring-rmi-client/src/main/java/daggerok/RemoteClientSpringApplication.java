package daggerok;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import daggerok.api.User;
import daggerok.api.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonMap;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.created;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@SpringBootApplication
public class RemoteClientSpringApplication {

  public static void main(String[] args) {
    SpringApplication.run(RemoteClientSpringApplication.class, args);
  }

  @Bean ObjectMapper objectMapper() {
    final ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    return objectMapper;
  }

  @Bean RmiProxyFactoryBean userService(@Value("${rmi.server.host:0.0.0.0}") final String rmiServerHost) {
    final RmiProxyFactoryBean rmiProxyFactoryBean = new RmiProxyFactoryBean();
    rmiProxyFactoryBean.setServiceUrl(format("rmi://%s:1199/UserService", rmiServerHost));
    rmiProxyFactoryBean.setServiceInterface(UserService.class);
    rmiProxyFactoryBean.setLookupStubOnStartup(false); // fail safe on startup
    return rmiProxyFactoryBean;
  }

  @Bean RouterFunction<ServerResponse> routes(final UserService userService) {
    return

        route(GET("/api/v1/users/{uuid}"),
              request -> ok().contentType(APPLICATION_JSON_UTF8)
                             .body(Mono.just(request.pathVariable("uuid"))
                                       .map(UUID::fromString)
                                       .map(userService::getUser)
                                       .subscribeOn(Schedulers.elastic()),
                                   User.class))

            .andRoute(GET("/api/v1/users"),
                      request -> ok().contentType(APPLICATION_STREAM_JSON)
                                     .body(Flux.fromIterable(userService.getAllUsers())
                                               .subscribeOn(Schedulers.elastic()), User.class))

            .andRoute(POST("/api/v1/users"),
                      request -> request.bodyToMono(User.class)
                                        .map(userService::saveUser)
                                        .subscribeOn(Schedulers.elastic())
                                        .map(uuid -> format("%s%s/%s", getBaseUrl(request), "/api/v1/users", uuid))
                                        .map(URI::create)
                                        .flatMap(uri -> created(uri).build()))

            .andRoute(GET("/*"),
                      request -> ok().body(Mono.just(getBaseUrl(request))
                                               .map(baseUrl -> asList(singletonMap("GET", pathTo(baseUrl, "/")),
                                                                      singletonMap("GET", pathTo(baseUrl, "/api/v1/users")),
                                                                      singletonMap("GET", pathTo(baseUrl, "/api/v1/users/{uuid}")),
                                                                      singletonMap("POST", pathTo(baseUrl, "/api/v1/users")))),
                                           List.class))
        ;
  }

  private String getBaseUrl(final ServerRequest request) {
    final URI uri = request.uri();
    final int last = uri.toString().length() - request.path().length();
    return uri.toString().substring(0, last);
  }

  private String pathTo(final String baseUrl, final String path) {
    return format("%s%s", baseUrl, path);
  }
}
