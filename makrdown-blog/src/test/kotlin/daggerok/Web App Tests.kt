package daggerok

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [App::class], webEnvironment = RANDOM_PORT)
class `Web App Tests`(@Autowired val client: TestRestTemplate) {
  @BeforeAll fun `before all`() = println("before all")
  @BeforeEach fun `before each`() = println("before each")

  @Test fun `home page should be as expected`() {
    val entity = client.getForEntity<String>("/")
    assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
    assertThat(entity.body).containsIgnoringCase("<title>Blog</title>")
    assertThat(entity.body).containsIgnoringCase("/article/2")
    assertThat(entity.body).containsIgnoringCase("<article><p><strong>headline trololo</strong></p>")
    assertThat(entity.body).containsIgnoringCase("/article/1")
    assertThat(entity.body).containsIgnoringCase("<article><p><strong>headline ololo</strong></p>")
  }

  @Test fun `article page should be as expected`() {
    val entity = client.getForEntity<String>("/article/1")
    assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
    assertThat(entity.body).containsIgnoringCase("<title>Blog</title>")
    assertThat(entity.body).containsIgnoringCase("<h2>ololo</h2>")
    assertThat(entity.body).containsIgnoringCase("<main><h1>content ololo</h1>")
  }

  @Test fun `test 2`() = println("test 2")
  @AfterEach fun `after each`() = println("after each")
  @AfterAll fun `after all`() = println("after all")
}
