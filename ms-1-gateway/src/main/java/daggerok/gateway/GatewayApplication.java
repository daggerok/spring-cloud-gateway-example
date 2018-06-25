package daggerok.gateway;

import daggerok.props.PropsAutoConfiguration;
import daggerok.props.PropsAutoConfiguration.Props;
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
  final Props props;

  @Bean
  RouteLocator msRouteLocator(final RouteLocatorBuilder builder) {
    return builder
        .routes()

        // step 5: after step 4 migration is done. monolithic app at this point of time could be completely disabled.

        // step 4: forward rest api calls to ms-3-rest micro-service
        .route("ms-3-rest", p -> p
            .path("/api/**")
            .uri(props.getRest().getUrl()))

        // step 3: everything else (except itself gateway actuator endpoints) forward to ms-2-ui micro-service
        .route("ms-2-ui", p -> p
            .path("/actuator/**")
            .negate()
            .uri(props.getUi().getUrl()))

        /*

        // step 2: oops, gateway actuator endpoints should respond by themselves, but not with monolith's...
        .route("ms-1-gateway", p -> p
            .path("/actuator/**")
            .negate()
            .uri(props.getMonolith().getUrl()))

        // step 1: forward everything to monolith app
        .route("ms-0-monolith", p -> p
            .path("/**")
            .uri(props.getMonolith().getUrl()))

        */

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
