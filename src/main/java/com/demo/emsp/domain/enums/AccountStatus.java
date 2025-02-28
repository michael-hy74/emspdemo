package com.demo.emsp.domain.enums;

import java.util.Arrays;

public enum AccountStatus {
    CREATED, ACTIVATED, DEACTIVATED;

    public static AccountStatus fromString(String value) {
        return Arrays.stream(values())
                .filter(status -> status.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid account status: " + value));
    }
}