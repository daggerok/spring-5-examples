package com.example.optionalmvcpathvariable;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Map;

@RestController
@SpringBootApplication
public class OptionalMvcPathVariableApplication {

    @GetMapping
    ResponseEntity<Map<String, String>> entryPoint(HttpServletRequest request) {
        var uri = URI.create(request.getRequestURL().toString());
        var baseUrl = String.format("GET %s://%s", uri.getScheme(), uri.getAuthority());
        return ResponseEntity.ok(Map.of("base URL GET", String.format("%s", baseUrl),
                                        "springdoc openapi ui GET", String.format("%s/swagger-ui.html", baseUrl),
                                        "springdoc openapi core GET", String.format("%s/v3/api-docs/", baseUrl),
                                        "non nullable GET", String.format("%s/non-nullable", baseUrl),
                                        "non nullable identifier GET", String.format("%s/non-nullable/{identifier}", baseUrl),
                                        "nullable GET", String.format("%s/nullable", baseUrl),
                                        "nullable identifier GET", String.format("%s/nullable/{identifier}", baseUrl),
                                        "optional GET", String.format("%s/optional", baseUrl),
                                        "optional identifier GET", String.format("%s/optional/{identifier}", baseUrl)));
    }

    public static void main(String[] args) {
        SpringApplication.run(OptionalMvcPathVariableApplication.class, args);
    }
}
