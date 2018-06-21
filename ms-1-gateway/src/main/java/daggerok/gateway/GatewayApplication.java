package daggerok.gateway;

import daggerok.props.PropsAutoConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.config.GatewayAutoConfiguration;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Log4j2
@Configuration
@RequiredArgsConstructor
@ComponentScan(basePackageClasses = GatewayAutoConfiguration.class)
class GatewayRoutesConfig {

  //@formatter:off
  //tag::content[]
  final PropsAutoConfiguration.Props props;

  @Bean
  RouteLocator msRouteLocator(RouteLocatorBuilder builder) {
    return builder
        .routes()

        /*
        // step 1.1: forward everything to monolith app
        .route("monolith", p -> p
            .path("/**")
            .uri(props.getMonolith().getUrl()))
        */

        // step 1.2: oops, gateway actuator endpoints should respond by themselves, but not with monolith's...
        .route("self-actuator", p -> p
            .path("/actuator/**")
            .negate()
            .uri(props.getMonolith().getUrl()))

        .build();
  }
  //end::content[]
  //@formatter:on
}

@Import({
    PropsAutoConfiguration.class,
})
@SpringBootApplication
//@SpringCloudApplication
public class GatewayApplication {

  public static void main(String[] args) {
    SpringApplication.run(GatewayApplication.class, args);
  }
}
