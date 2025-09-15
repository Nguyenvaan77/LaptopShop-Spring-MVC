package com.basis.anhangda37;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class HelloController {

    @GetMapping("/")
    public String index() {
        return "Hello world from Spring boot";
    }
}
