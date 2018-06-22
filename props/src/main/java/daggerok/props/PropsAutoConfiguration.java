package daggerok.props;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class PropsAutoConfiguration {

  @Data
  @Component
  @NoArgsConstructor
  @Accessors(chain = true)
  @ConfigurationProperties(prefix = "props")
  public static class Props {

    private App monolith, gateway, ui, rest;

    @Data
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class App {

      private String url, proto, host;
      private Integer port;
    }
  }
}
