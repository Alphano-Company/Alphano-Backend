package com.alphano.alphano.problem.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProblemController {

    @GetMapping("/")
    ResponseEntity<String> test() {
        return ResponseEntity.ok("Hello World");
    }
}
