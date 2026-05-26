package com.sugarylatics.springbootprac;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Component;

@Component // meaning this a singleton
public class RateLimiter {
    private final long windowMs = 5_000;
    private final int maxHits = 5;
    private final ConcurrentHashMap<String, Deque<Long>> windows = new ConcurrentHashMap<>();
    private final AtomicLong totalRequests = new AtomicLong();
    private final AtomicLong rateLimitedRequests = new AtomicLong();

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
}
