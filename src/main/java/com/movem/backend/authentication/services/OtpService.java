package com.movem.backend.authentication.services;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {

    private final Map<String, OtpEntry> otpStore = new ConcurrentHashMap<>();
    private final SecureRandom secureRandom = new SecureRandom();

    public String generateOtp(String username) {
        String key = normalizeUsername(username);
        String code = String.format("%06d", secureRandom.nextInt(1_000_000));

        long expiryTime = System.currentTimeMillis() + (5 * 60 * 1000);
        otpStore.put(key, new OtpEntry(code, expiryTime));

        return code;
    }

    public boolean verifyOtp(String username, String submittedCode) {
        String key = normalizeUsername(username);
        OtpEntry entry = otpStore.get(key);

        if (entry == null) {
            return false;
        }
        if (System.currentTimeMillis() > entry.expiryTime()) {
            otpStore.remove(key);
            return false;
        }
        boolean matches = entry.code().equals(submittedCode.trim());

        if (matches) {
            otpStore.remove(key);
        }
        return matches;
    }

    private String normalizeUsername(String username) {
        return username.trim().toLowerCase();
    }
    private record OtpEntry(String code, long expiryTime) {}
}