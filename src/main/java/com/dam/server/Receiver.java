package com.dam.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class Receiver {
    @Autowired
    SecurityConfig securityConfig;

    @GetMapping("/test")
    public String test() {
        UserDetails playerDetail = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return "Hello World, time stamp: " + new Date() + "\nYou are " + playerDetail.getUsername();
    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterBody registerBody) {
        return "";
    }

    @PostMapping("/lineup")
    public String lineup(){
        return "";
    }

    @GetMapping("/lineup")
    public String lineup2(){
        return "";
    }

}
