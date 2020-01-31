package daggerok.junit4;

import daggerok.App;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Slf4j
@RunWith(
    SpringRunner.class
)
@SpringBootTest(
    classes = App.class,
    webEnvironment = RANDOM_PORT
)
public class AppJUnit4Tests {

  @LocalServerPort
  Integer port;

  @Test
  public void testContext() {
    log.info("Dummy test is running on {} port.", port);
  }
}
