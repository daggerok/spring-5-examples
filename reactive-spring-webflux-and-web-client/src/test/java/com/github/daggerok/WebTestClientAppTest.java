package com.github.daggerok;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@Log4j2
@Disabled // this one is going to fail on random ports
@AutoConfigureWebTestClient                                                     // 1
@DisplayName("WebClient Test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)    // 2
class WebTestClientAppTest {

  @Autowired WebTestClient webTestClient;                                       // 3

  @Test
  void test() {
    webTestClient.get().uri("/api/v1/collect")                               // 4
                 .exchange()
                 .expectStatus().isOk()
                 .expectBody(String.class)
                 .consumeWith(result -> {
                   final String body = result.getResponseBody();
                   log.info("body: {}", body);
                   assertThat(body).isGreaterThanOrEqualTo("HEY response: hey! - HOY response: hoy!");
                 })
    ;
  }
}
