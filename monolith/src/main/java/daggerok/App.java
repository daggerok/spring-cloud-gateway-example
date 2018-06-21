package daggerok;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import static java.util.Collections.singletonList;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.*;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Data
@Entity
@NoArgsConstructor
@Accessors(chain = true)
class Content implements Serializable {

  private static final long serialVersionUID = -7618202574843387015L;

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  String id;

  String body;
}

interface Contents extends JpaRepository<Content, String> {}

@Log4j2
@Component
@RequiredArgsConstructor
class CreateSomeContent implements ApplicationRunner {

  final Contents contents;

  @Override
  @Transactional
  public void run(ApplicationArguments args) throws Exception {
    IntStream.range(0, 3)
        .mapToObj(String::valueOf)
        .map(s -> UUID.randomUUID())
        .map(String::valueOf)
        .map(it -> it + it + it + it + it)
        .map(body -> new Content().setBody(body))
        .map(contents::save)
        .forEach(content -> log.info("content {}", content::toString));
  }
}

@Controller
@RequiredArgsConstructor
class IndexPage {

  final Contents contents;

  @GetMapping("/")
  String index() {
    return "index";
  }
}

@Log4j2
@Configuration
@RequiredArgsConstructor
class WebfluxRoutesConfig {

  final Contents contents;

  @Bean
  HandlerFunction<ServerResponse> contentsHandler() {
    return request -> {
      final Flux<Content> publisher = Flux.fromIterable(contents.findAll());
      final ParameterizedTypeReference<Content> type = new ParameterizedTypeReference<Content>() {};
      return ok().body(publisher, type);
    };
  }

  @Bean
  HandlerFunction<ServerResponse> fallbackHandler() {
    return request -> {
      final ParameterizedTypeReference<List<String>> type = new ParameterizedTypeReference<List<String>>() {};
      final List<String> api = singletonList("GET contents -> /api/contents");
      final Mono<List<String>> publisher = Mono.just(api);
      return ok().body(publisher, type);
    };
  }

  @Bean
  RouterFunction routes(final HandlerFunction<ServerResponse> fallbackHandler) {
    return
        nest(
            path("/api"),
            route(
                GET("/contents"),
                contentsHandler()
            )
        ).andRoute(
            GET("/api/**"),
            fallbackHandler
        ).andOther(
            resources("/**", new ClassPathResource("public/"))
        )
        ;
  }
}

@SpringBootApplication
public class App {

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }
}
