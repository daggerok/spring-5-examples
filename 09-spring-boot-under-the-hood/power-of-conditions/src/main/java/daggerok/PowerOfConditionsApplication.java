package daggerok;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PowerOfConditionsApplication {

  public static void main(String[] args) {
    SpringApplication.run(PowerOfConditionsApplication.class, args);
  }

  @Bean
  public String string() {
    System.out.println("string");
    return "string";
  }

  @Bean
  @ConditionalOnClass(name = "java.lang.String")
  public String javaString() {
    System.out.println("java.lang.String");
    return "java string";
  }

  @Bean
  @ConditionalOnClass(name = "ololo.Trololo")
  public String missingString1() {
    System.out.println("don't see me");
    return "you will never see me... =(";
  }

  @Bean
  @ConditionalOnMissingClass("ololo.Trololo")
  public String missingString2() {
    System.out.println("see me");
    return "but will see me =)";
  }
}
