package daggerok.rest;

import daggerok.data.Activity;
import daggerok.data.ActivityRepository;
import daggerok.data.Task;
import daggerok.data.TaskRepository;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.ReactiveRedisConnection;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static java.lang.String.format;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class RoutesConfig {

  @Bean
  public RouterFunction<ServerResponse> routes(final TaskRepository taskRepository,
                                               final ActivityRepository activityRepository,
                                               final ReactiveRedisConnection connection) {

    val keyCommands = connection.keyCommands();

    return

        route(

            DELETE("/"),
            request -> ok().body(
                Mono.fromCallable(() -> {
                  activityRepository.deleteAll();
                  taskRepository.deleteAll();
                  return "done.";
                }), String.class))

        .andRoute(

            GET("/tasks"),
            request -> ok().body(Flux.fromIterable(taskRepository.findAll()), Task.class))

        .andRoute(

            GET("/activities"),
            request -> ok().body(Flux.fromIterable(activityRepository.findAll()), Activity.class))

        .andRoute(

            GET("/**"),
            request -> ok().body(Mono.just(format("command type %s",
                                                  keyCommands.randomKey()
                                                             .flatMap(keyCommands::type)
                                                             .map(DataType::code)
                                                             .subscribe())), String.class))
        ;
  }
}
