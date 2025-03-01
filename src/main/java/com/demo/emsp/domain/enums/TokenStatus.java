package com.demo.emsp.domain.enums;

import java.util.Arrays;

public enum TokenStatus {
    CREATED, ASSIGNED, ACTIVATED, DEACTIVATED;

    public static TokenStatus fromString(String value) {
        if (value == null) {
            return CREATED;
        }
        return Arrays.stream(values())
                .filter(status -> status.name().equalsIgnoreCase(value.trim()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid token status: " + value));
    }
}