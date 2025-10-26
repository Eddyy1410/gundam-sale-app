package com.huyntd.superapp.gundam_shop.service.authentication;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

/**
 * Simple in-memory nonce store with TTL. This avoids adding DB columns/entities.
 * Note: in-memory store is not shared across app instances and will be cleared on restart.
 */
@Component
public class ResetNonceStore {

    private static class Entry {
        final String nonce;
        final long expiresAt; // epoch seconds

        Entry(String nonce, long expiresAt) {
            this.nonce = nonce;
            this.expiresAt = expiresAt;
        }
    }

    private final Map<String, Entry> map = new ConcurrentHashMap<>();

    public void put(String email, String nonce, long ttlSeconds) {
        long exp = Instant.now().getEpochSecond() + Math.max(1, ttlSeconds);
        map.put(email.toLowerCase(), new Entry(nonce, exp));
    }

    public String get(String email) {
        Entry e = map.get(email.toLowerCase());
        if (e == null) return null;
        if (e.expiresAt < Instant.now().getEpochSecond()) {
            map.remove(email.toLowerCase());
            return null;
        }
        return e.nonce;
    }

    public void remove(String email) {
        map.remove(email.toLowerCase());
    }
}
