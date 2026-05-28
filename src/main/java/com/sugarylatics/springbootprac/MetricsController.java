package com.sugarylatics.springbootprac;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;

@RestController
public class MetricsController  {

    private final RateLimiter rateLimiter;

    public MetricsController(RateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
    }
    @GetMapping (value = "/metrics", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getMetrics(){
        return "# HELP requests_total Total number of requests\n" +
                "# TYPE requests_total counter\n" +
                "requests_total "+ rateLimiter.getTotalRequests() + "\n" +

                "# HELP active_ips Current number of tracked IPs\n" +
                "# TYPE active_ips gauge\n" +
                "active_ips " + rateLimiter.getActiveIpCount() + "\n" +

                "# HELP rate_limit_hits Total rate-limited requests\n" +
                "# TYPE rate_limit_hits counter\n" +
                "rate_limit_hits "+ rateLimiter.getRateLimitedRequests() +"\n" +

                "# HELP request_latency_p95_us 95th percentile request latency in microseconds\n" +
                "# TYPE request_latency_p95_us gauge\n" +
                "request_latency_p95_us "+ rateLimiter.getP95Latency() +"\n" ;
    }
}
