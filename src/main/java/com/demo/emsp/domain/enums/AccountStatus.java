package com.demo.emsp.domain.enums;

import java.util.Arrays;

public enum AccountStatus {
    CREATED, ACTIVATED, DEACTIVATED;

    public static AccountStatus fromString(String value) {
        if (value == null) {
            return CREATED;
        }
        return Arrays.stream(values())
                .filter(status -> status.name().equalsIgnoreCase(value.trim()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid account status: " + value));
    }
}