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
        long start = System.nanoTime();
        if (!"secret123".equals(auth)) {
            long latencyMs = (System.nanoTime() - start) / 1_000;
            rateLimiter.recordLatency(latencyMs);
            return ResponseEntity.status(401).body(Map.of("error", "unauthorized"));
        }
        String ip = "127.0.0.1";
        if (!rateLimiter.isAllowed(ip)) {
            long latencyMs = (System.nanoTime() - start) / 1_000;
            rateLimiter.recordLatency(latencyMs);
            return ResponseEntity.status(429).body(Map.of("error","rate_limited"));
        }
        long latencyMs = (System.nanoTime() - start ) / 1_000;
        rateLimiter.recordLatency(latencyMs);
        return ResponseEntity.ok(Map.of("status", "ok", "message","pongd"));
    }

}