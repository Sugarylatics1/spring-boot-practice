package com.sugarylatics.springbootprac.controller;

import com.sugarylatics.springbootprac.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService){
        this.authService = authService;
    }
    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestParam String email, @RequestParam String password)
    {
        try {
            String apiKey = authService.register(email,password);
            return ResponseEntity.ok(Map.of(
                "message","User registered successfully",
                "apiKey", apiKey,
                "tier", "BASIC"
            ));
        } catch (RuntimeException e) {
        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
