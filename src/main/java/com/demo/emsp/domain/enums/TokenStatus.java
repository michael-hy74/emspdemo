package com.demo.emsp.domain.enums;

public enum TokenStatus {
    CREATED, ASSIGNED, ACTIVATED, DEACTIVATED;

    public static TokenStatus fromString(String text) {
        return TokenStatus.valueOf(text.toUpperCase());
    }
}