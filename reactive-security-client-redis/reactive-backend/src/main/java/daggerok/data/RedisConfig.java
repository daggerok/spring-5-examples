package daggerok.data;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnection;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories
public class RedisConfig {

  @Bean
  public LettuceConnectionFactory lettuceConnectionFactory() {
    return new LettuceConnectionFactory();
  }

  @Bean
  public RedisConnectionFactory redisConnectionFactory() {
    return lettuceConnectionFactory();
  }

  @Bean
  public ReactiveRedisConnectionFactory reactiveRedisConnectionFactory() {
    return lettuceConnectionFactory();
  }

  @Bean
  public ReactiveRedisConnection reactiveRedisConnection() {
    return reactiveRedisConnectionFactory().getReactiveConnection();
  }
}
