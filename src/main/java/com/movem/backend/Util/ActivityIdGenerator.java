package com.movem.backend.Util;

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