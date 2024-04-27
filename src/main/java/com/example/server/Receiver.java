package com.example.server;

import com.example.server.request.LoginRequest;
import com.example.server.response.LoginResponse;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class Receiver {
    @Autowired
    private TokenService tokenService;

    //test server state
    @GetMapping("/test/?*/{message}")
    public String test(@PathVariable String message) {
        return message;
    }

    //token getting
    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {

        LoginResponse loginResponse = tokenService.createToken(loginRequest);
        
        return ResponseEntity.ok(loginResponse);
    }
}
