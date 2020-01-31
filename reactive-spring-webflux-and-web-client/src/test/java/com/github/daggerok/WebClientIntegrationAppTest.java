package com.github.daggerok;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import static java.lang.String.format;

/*
// Junit 4:
import org.junit.Test;

public class WebClientIntegrationAppTest {

  @Test
  public void main() {
    GenericApplicationContext ctx = new AnnotationConfigApplicationContext(App.class);
    assertThat(ctx).isNotNull();

    Function<String, String> greeter = ctx.getBean(Function.class);
    assertThat(greeter.apply("Test")).isNotNull()
                                     .isEqualTo("hello, Test!");
  }
}
*/
// Junit 5 (Jupiter):

// https://docs.spring.io/spring-boot/docs/2.0.0
// .BUILD-SNAPSHOT/reference/htmlsingle/#boot-features-testing-spring-boot-applications-testing-autoconfigured
// -webflux-tests

@Log4j2
@DisplayName("WeClient 5 Test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class WebClientIntegrationAppTest {

  @Test
  void main() {
    String url = format("http://127.0.0.1:%d", 8080);
    log.info("url: {}", url);
    StepVerifier.create(WebClient.create(url).get().uri("/api/v1/collect")
                                 .exchange().flatMap(clientResponse -> clientResponse.bodyToMono(String.class))
                                 .doOnNext(log::info))
                .expectNext("HEY response: hey! - HOY response: hoy!")
                .verifyComplete();
  }
}
