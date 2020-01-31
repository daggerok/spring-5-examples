package com.example.optionalmvcpathvariable.nullable;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.result.view.Rendering;

@Controller
public class NonNullableController {

    @GetMapping({ "/non-nullable/{id}" })
    Rendering nonNullable(@PathVariable("id") String id) {
        return Rendering.view("non-nullable")
                        .modelAttribute("identifier", String.format("non-nullable: %s", id))
                        .build();
    }

    @GetMapping({ "/non-nullable" })
    Rendering nonNullable() {
        return Rendering.view("non-nullable")
                        .modelAttribute("identifier", "not-nullable")
                        .build();
    }
}
