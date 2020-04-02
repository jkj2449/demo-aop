package com.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello(@RequestParam Map<String, String> test) {
        return "hello";
    }
}
