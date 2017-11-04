package daggerok;

import com.fasterxml.jackson.databind.ObjectMapper;
import daggerok.data.Activity;
import daggerok.data.ActivityRepository;
import daggerok.data.Task;
import daggerok.data.TaskRepository;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

@Slf4j
@SpringBootApplication
public class ReactiveWebfluxSpringDataRedisApplication {

  @Configuration
  public static class TestDataConfig {

    @Autowired
    ObjectMapper objectMapper;

    private String json(Object o) {

      return Try.of(() -> objectMapper.writerWithDefaultPrettyPrinter()
                                      .writeValueAsString(o))
                .get();
    }

    @Bean
    @Transactional
    public CommandLineRunner testData(final TaskRepository taskRepository,
                                      final ActivityRepository activityRepository) {

      val items = Stream.of(taskRepository.findAll(),
                            activityRepository.findAll())
                        .map(Iterable::spliterator)
                        .map(spliterator -> StreamSupport.stream(spliterator, false))
                        .flatMap(stream -> stream)
                        .map(this::json)
                        .collect(toList());

      items.forEach(this::json);

      if (items.size() > 4) {
        activityRepository.deleteAll();
        taskRepository.deleteAll();
      }

      return args -> Flux.just("one", "two", "three")
                         .map(i -> new Task().setBody(i))
                         .map(taskRepository::save)
                         .collectList()
                         .map(l -> new Activity().setTasks(l))
                         .map(activityRepository::save)
                         .subscribe();
    }
  }

  public static void main(String[] args) {
    SpringApplication.run(ReactiveWebfluxSpringDataRedisApplication.class, args);
  }
}
