package com.example.JWTPractise.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnotherController {

    @GetMapping("/hi")
    public String getSomething() {
        return "Something";
    }
}
