package com.github.daggerok;

import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@Slf4j
@Ignore // this one is going to fail on random ports
@AutoConfigureWebTestClient
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CollectWebTestClientTest {

  @Autowired
  private WebTestClient webTestClient;

  @Test
  public void collect() {
    webTestClient.get().uri("/api/v1/collect")
                 .accept(MediaType.TEXT_PLAIN)
                 .exchange().expectStatus().isOk()
                 .expectBody(String.class).isEqualTo("HEY: hoy! - HOY: hoy!")
                 .consumeWith(result -> log.info("res: {}", result.getResponseBody()));
  }
}
