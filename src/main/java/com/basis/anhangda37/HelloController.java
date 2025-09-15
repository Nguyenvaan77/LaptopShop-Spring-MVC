package com.basis.anhangda37;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class HelloController {

    @GetMapping("/")
    public String index() {
        return "Hello world from Spring ";
    }

    @GetMapping("/user")
    public String indexUser() {
        return "Only User con access this api";
    }   
    
    @GetMapping("/admin")
    public String indexAdmin() {
        return "Only Admin con access this api";
    }
}
