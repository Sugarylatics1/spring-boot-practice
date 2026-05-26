package com.sugarylatics.springbootprac;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.http.ResponseEntity;
import java.util.Map;

@RestController
public class PingController {
    private final RateLimiter rateLimiter;
    public PingController(RateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    @GetMapping("/ping")
    public ResponseEntity<Map<String, String>> ping(@RequestHeader("Authorization") String auth) {
        if (!"secret123".equals(auth)) {return ResponseEntity.status(401).body(Map.of("error", "unauthorized"));}
        String ip = "127.0.0.1";
        if (!rateLimiter.isAllowed(ip)) {
            return ResponseEntity.status(429).body(Map.of("error","rate_limited"));
        }
        return ResponseEntity.ok(Map.of("status", "ok", "message","pongd"));
    }

}