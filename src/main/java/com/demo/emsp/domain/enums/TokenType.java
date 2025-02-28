package com.demo.emsp.domain.enums;

import java.util.Arrays;

public enum TokenType {
    RFID, EMAID, MAC_ADDRESS;

    public static TokenType fromString(String value) {
        return Arrays.stream(values())
                .filter(status -> status.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid token type: " + value));
    }
}