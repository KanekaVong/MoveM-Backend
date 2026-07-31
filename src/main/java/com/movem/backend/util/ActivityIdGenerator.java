package com.movem.backend.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ActivityIdGenerator {

    public String generate() {

        return "ACT" + UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 7)
                .toUpperCase();
    }
}