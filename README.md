# Java API Gateway

Production-grade rate limiter and monitoring dashboard built with Spring Boot 4.0.6, React, and in-memory sliding window algorithms.

##  Live Demo
- Dashboard: http://40.81.193.87:8080/
- Metrics: http://40.81.193.87:8080/metrics
- Ping Endpoint: `curl -H "Authorization: secret123" http://40.81.193.87:8080/ping`

## Architecture
- **Rate Limiter**: Sliding window algorithm (`ArrayDeque` + `ConcurrentHashMap`)
- **Metrics**: Prometheus-compatible `/metrics` endpoint (counters, gauges, p95 latency)
- **Frontend**: React + Vite dashboard with auto-refresh
- **Backend**: Spring Boot 4.0.6, Java 26, Tomcat

## Performance
| Metric | Value |
|--------|-------|
| Algo overhead | ~10 µs per request |
| P95 latency (low load) | 12 ms |
| Throughput (c=10) | 1,616 RPS |
| Rate limit enforcement | 99.8% accuracy |

## ️ Tech Stack
`Java 26` `Spring Boot 4.0.6` `React` `Vite` `Apache Bench` `Azure VPS`

## License
MIT