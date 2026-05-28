package com.sugarylatics.springbootprac;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Component;

@Component // meaning this a singleton
public class RateLimiter {
    private final long windowMs = 5_000;
    private final int maxHits = 5;
    private final ConcurrentHashMap<String, Deque<Long>> windows = new ConcurrentHashMap<>();
    private final AtomicLong totalRequests = new AtomicLong();
    private final AtomicLong rateLimitedRequests = new AtomicLong();
    private final List<Long> recentLatencies = Collections.synchronizedList(new ArrayList<>());

    public boolean isAllowed(String ip) {
        totalRequests.incrementAndGet();
        long now = System.currentTimeMillis();
        Deque<Long> deque = windows.computeIfAbsent(ip, k -> new ArrayDeque<>());
        synchronized (deque) {
            while (!deque.isEmpty() && now - deque.peekFirst() > windowMs) {
                deque.pollFirst();
            }
            if (deque.size() >= maxHits) {
                rateLimitedRequests.incrementAndGet();
                return false;
            }
            deque.addLast(now);
            return true;
        }
    }
    public int getActiveIpCount() { return windows.size();}
    public long getTotalRequests() { return totalRequests.get();}
    public long getRateLimitedRequests() { return rateLimitedRequests.get();}

    public void recordLatency(long latencyMs) {
        recentLatencies.add(latencyMs);
        if (recentLatencies.size() > 1000) recentLatencies.remove(0);
    }

    public long getP95Latency() {
        if(recentLatencies.isEmpty()) return 0;
        List<Long> sorted = new ArrayList<>(recentLatencies);
        Collections.sort(sorted);
        int idx = (int) (sorted.size() * 0.95);
        return sorted.get(idx);
    }

}
