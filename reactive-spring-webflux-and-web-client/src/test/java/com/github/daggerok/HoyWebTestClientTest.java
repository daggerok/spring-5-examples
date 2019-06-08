package com.github.daggerok;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@AutoConfigureWebTestClient
@RunWith(SpringRunner.class)
//  We create a `@SpringBootTest`, starting an actual server on a `RANDOM_PORT`
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HoyWebTestClientTest {

  // Spring Boot will create a `WebTestClient` for you,
  // already configure and ready to issue requests against "localhost:RANDOM_PORT"
  @Autowired
  private WebTestClient webTestClient;

  @Test
  public void hoy() {
    webTestClient
        // Create a GET request to test an endpoint
        .get().uri("/api/v1/hoy")
        .accept(MediaType.TEXT_PLAIN)
        .exchange()
        // and use the dedicated DSL to test assertions against the response
        .expectStatus().isOk()
        .expectBody(String.class).isEqualTo("hoy!");
  }
}
