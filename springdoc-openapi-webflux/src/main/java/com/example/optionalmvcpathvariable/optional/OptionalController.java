package com.example.optionalmvcpathvariable.optional;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.result.view.Rendering;

import java.util.Optional;

@Controller
public class OptionalController {

    @GetMapping({ "/optional", "/optional/{id}" })
    Rendering optional(@PathVariable("id") Optional<String> id) {
        return Rendering.view("optional")
                        .modelAttribute("identifier", id.map("non-empty: "::concat)
                                                        .orElse("empty optional"))
                        .build();
    }
}
