package com.example.optionalmvcpathvariable.nullable;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.result.view.Rendering;

import java.util.Optional;

@Controller
public class NullableController {

    @GetMapping({ "/nullable", "/nullable/{id}" })
    Rendering optional(@PathVariable(name = "id", required = false) String id) {
        return Rendering.view("nullable")
                        .modelAttribute("identifier", Optional.ofNullable(id)
                                                              .map("nullable: "::concat)
                                                              .orElse("nullable"))
                        .build();
    }
}
