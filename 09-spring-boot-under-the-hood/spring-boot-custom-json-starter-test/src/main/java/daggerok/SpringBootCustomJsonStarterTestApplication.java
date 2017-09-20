package daggerok;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import daggerok.json.Json;

import java.util.Date;

@Slf4j
@SpringBootApplication
public class SpringBootCustomJsonStarterTestApplication {

  @Data
  @Accessors(chain = true)
  public static class Hello {
    String value;
  }

  @Bean
  @ConditionalOnProperty(name = "spring.custom-json.enable", matchIfMissing = false, havingValue = "true")
  public String testBuiltIn(final Json json) {

    final String dateJsonString = "1505853225044";
    final Date date = json.parse(dateJsonString, Date.class);
    log.warn("date: '{}' from JSON string: '{}'", date, dateJsonString);

    final String result = json.stringify(date);
    log.warn("JSON string: '{}' from date: '{}'", result, date);
    return result;
  }

  @Bean
  @ConditionalOnProperty(name = "spring.custom-json.enable", matchIfMissing = false, havingValue = "true")
  public Hello testCustom(final Json json) {

    final Hello hello = new Hello().setValue("test");
    final String helloJsonString = json.stringify(hello);
    log.warn("JSON string: '{}' from hello: '{}'", helloJsonString, hello);

    final Hello result = Hello.class.cast(json.parse(helloJsonString, Hello.class));
    log.warn("hello: '{}' from JSON string: '{}'", result, helloJsonString);
    return result;
  }

  public static void main(String[] args) {
    SpringApplication.run(SpringBootCustomJsonStarterTestApplication.class, args);
  }
}
