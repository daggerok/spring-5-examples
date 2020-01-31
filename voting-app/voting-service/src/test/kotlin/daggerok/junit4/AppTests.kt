package daggerok.junit4

import daggerok.App
import org.junit.Test
import org.springframework.boot.test.context.SpringBootTest
import org.junit.runner.RunWith
import org.springframework.test.context.junit4.SpringRunner

@RunWith(
  SpringRunner::class
)
@SpringBootTest(
  classes = [App::class],
  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AppTests {

  @Test
  fun contextLoads() {}
}
