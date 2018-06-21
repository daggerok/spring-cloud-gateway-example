package daggerok.monolith.junit5;

import daggerok.gateway.GatewayApplication;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Slf4j
@ExtendWith(
    SpringExtension.class
)
@SpringBootTest(
    classes = GatewayApplication.class,
    webEnvironment = RANDOM_PORT
)
@RequiredArgsConstructor
public class GatewayApplicationJUnit5Tests {

  @LocalServerPort
  Integer port;

  @Test
  @DisplayName("Testing application context")
  void testContext() {
    log.info("Dummy test is running on {} port.", port);
  }
}
