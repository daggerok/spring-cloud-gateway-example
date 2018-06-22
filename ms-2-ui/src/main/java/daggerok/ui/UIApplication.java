package daggerok.ui;

import daggerok.props.PropsAutoConfiguration;
import daggerok.props.PropsAutoConfiguration.Props;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RouterFunction;

import java.util.Map;

import static org.springframework.http.MediaType.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.resources;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

//tag::index-spa[]
@Controller
@RequiredArgsConstructor
class IndexPage {

  @GetMapping("/")
  String index() {
    return "index";
  }
}
//end::index-spa[]

///*
//tag::proxy-api[]
@Log4j2
@Configuration
@RequiredArgsConstructor
class RestApiProxyConfig {

  final Props props;

  @Bean
  WebClient webClient() {
    return WebClient.create(props.getMonolith().getUrl());
  }

  @Bean
  RouterFunction routes(WebClient webClient) {

    final ParameterizedTypeReference<Map> maps = new ParameterizedTypeReference<Map>() {};
    final ParameterizedTypeReference<String> strings = new ParameterizedTypeReference<String>() {};

    return route(
        GET("/api/contents"),
        request -> ok().contentType(APPLICATION_JSON).body(webClient
            .get().uri("/api/contents")
            .accept(APPLICATION_JSON)
            .header("Content-Type", APPLICATION_JSON_VALUE)
            .retrieve().bodyToFlux(maps), maps)
    ).andRoute(
        GET("/api/**"),
        request -> ok().contentType(APPLICATION_JSON).body(webClient
            .get().uri("/api/")
            .accept(APPLICATION_JSON)
            .header("Content-Type", APPLICATION_JSON_VALUE)
            .retrieve().bodyToFlux(strings), strings)
    ).andOther(
        resources("/**", new ClassPathResource("public/"))
    );
  }
}
//end::proxy-api[]
//*/

@Import({
    PropsAutoConfiguration.class,
})
@SpringBootApplication
public class UIApplication {

  public static void main(String[] args) {
    SpringApplication.run(UIApplication.class, args);
  }
}
