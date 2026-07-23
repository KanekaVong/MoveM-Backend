package com.movem.backend.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {

    // username -> OTP details, held in memory
    private final Map<String, OtpEntry> otpStore = new ConcurrentHashMap<>();

    public String generateOtp(String username) {
        String code = String.format("%06d", new Random().nextInt(999999));
        long expiryTime = System.currentTimeMillis() + (5 * 60 * 1000); // 5 minutes
        otpStore.put(username, new OtpEntry(code, expiryTime));
        return code;
    }

    public boolean verifyOtp(String username, String submittedCode) {
        OtpEntry entry = otpStore.get(username);
        if (entry == null) return false;
        if (System.currentTimeMillis() > entry.expiryTime()) {
            otpStore.remove(username); // expired, clean up
            return false;
        }
        boolean matches = entry.code().equals(submittedCode);
        if (matches) otpStore.remove(username); // one-time use — remove after success
        return matches;
    }

    private record OtpEntry(String code, long expiryTime) {}
}