package com.example.optionalmvcpathvariable;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;

@RestController
@SpringBootApplication
public class OptionalMvcPathVariableApplication {

    @GetMapping("/")
    ResponseEntity<List<String>> fallback(ServerWebExchange exchange) {
        var uri = exchange.getRequest().getURI();
        var baseUrl = String.format("GET %s://%s", uri.getScheme(), uri.getAuthority());
        return ResponseEntity.ok(List.of(String.format("GET %s", baseUrl),
                                         String.format("GET %s/v3/swagger-ui.html", baseUrl),
                                         String.format("GET %s/v3/api-docs/", baseUrl),
                                         String.format("GET %s/non-nullable", baseUrl),
                                         String.format("GET %s/non-nullable/{identifier}", baseUrl),
                                         String.format("GET %s/nullable", baseUrl),
                                         String.format("GET %s/nullable/{identifier}", baseUrl),
                                         String.format("GET %s/optional", baseUrl),
                                         String.format("GET %s/optional/{identifier}", baseUrl)));
    }

    public static void main(String[] args) {
        SpringApplication.run(OptionalMvcPathVariableApplication.class, args);
    }
}
