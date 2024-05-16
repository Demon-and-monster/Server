package com.dam.server;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class Receiver {
    @GetMapping("/test")
    public String test() {
        return "Hello World, time stamp: " + new Date();
    }

}
