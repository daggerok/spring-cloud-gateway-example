package daggerok.rest.junit4;

import daggerok.rest.RestApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RestApplication.class, webEnvironment = RANDOM_PORT)
public class RestApplicationJUnit4Tests {

  @LocalServerPort
  Integer port;

  @Test
  public void testContext() {
    log.info("Dummy test is running on {} port.", port);
  }
}
