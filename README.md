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

## Docker Environment Setup

You can run the entire environment (database, cache, broker, backend API, and frontend client) using Docker Compose. Make sure Docker is running and execute:

```bash
docker compose up -d
```

This command builds and starts the following services:
- **`db` (Postgres)**: Exposed on port `5432`. Matches backend settings (database: `nexus`, username: `postgres`, password: `nexusdev`).
- **`redis` (Redis)**: Exposed on port `6379`.
- **`kafka` (Kafka)**: Run in KRaft mode and exposed on port `9092` (host) and `29092` (internal network).
- **`backend` (Spring Boot API)**: Exposed on port `8080` (automatically waits for the database to be healthy).
- **`frontend` (Vite dev server)**: Exposed on port `5173` (proxies `/ping`, `/metrics`, and `/auth` calls to the backend container). Live reloading is active via volume mapping.

## 🛠️ Tech Stack
`Java 26` `Spring Boot 4.0.6` `React` `Vite` `Apache Bench` `Azure VPS` `Docker` `PostgreSQL` `Redis` `Kafka`

## License
MIT