package com.sugarylatics.springbootprac;

import com.sugarylatics.springbootprac.model.UsageLog;
import com.sugarylatics.springbootprac.model.User;
import com.sugarylatics.springbootprac.repository.UsageLogRepository;
import com.sugarylatics.springbootprac.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import java.util.Optional;

@RestController
public class PingController {
    private final RateLimiter rateLimiter;
    private final UserRepository userRepository;
    private final UsageLogRepository usageLogRepository;

    public PingController(RateLimiter rateLimiter,
                          UserRepository userRepository,
                          UsageLogRepository usageLogRepository) {
        this.rateLimiter = rateLimiter;
        this.userRepository = userRepository;
        this.usageLogRepository = usageLogRepository;
    }
    @GetMapping("/ping")
    public ResponseEntity<Map<String, String>> ping(
            @RequestHeader(value="Authorization", required = false) String auth) {
        long start = System.nanoTime();
        Optional<User> userOpt = userRepository.findByApiKey(auth);
        if (auth == null || userOpt.isEmpty()) {
            long latencyMs = (System.nanoTime() - start) / 1_000;
            rateLimiter.recordLatency(latencyMs);
            return ResponseEntity.status(401).body(Map.of("error", "unauthorized"));
        }
        User user = userOpt.get();
        String ip = "127.0.0.1"; // todo: extract later
        if (!rateLimiter.isAllowed(ip)) {
            long latencyMs = (System.nanoTime() - start) / 1_000;
            rateLimiter.recordLatency(latencyMs);
            return ResponseEntity.status(429).body(Map.of("error","rate_limited"));
        }
        long latencyMs = (System.nanoTime() - start ) / 1_000;
        rateLimiter.recordLatency(latencyMs);
        logUsage(user, "/ping", 200, start);
        return ResponseEntity.ok(Map.of("status", "ok", "message","pongd"));
    }
    private void logUsage(User user, String endpoint, int statusCode, long startNs) {
        try {
            UsageLog log = new UsageLog();
            log.setUser(user);
            log.setEndpoint(endpoint);
            log.setStatusCode(statusCode);
            log.setLatencyUs((System.nanoTime() - startNs) / 1_000);
            usageLogRepository.save(log);
        } catch (Exception e) {
            System.err.println("Failed to log usage: " + e.getMessage());
        }
    }
}