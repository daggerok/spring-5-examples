package daggerok.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import daggerok.json.Json;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = Json.class)
public class CustomJsonAutoConfiguration {

  @Bean
  @ConditionalOnProperty(name = "spring.custom-json.enable", havingValue = "true")
  public Json json() {
    return new Json(objectMapper());
  }

  @ConditionalOnClass(ObjectMapper.class)
  @Bean(name = "daggerok.config.CustomJsonAutoConfiguration.ObjectMapper")
  public ObjectMapper objectMapper() {
    return new ObjectMapper();
  }
}
