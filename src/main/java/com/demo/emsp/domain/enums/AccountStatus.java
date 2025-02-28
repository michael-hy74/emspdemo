package com.demo.emsp.domain.enums;

public enum AccountStatus {
    CREATED, ACTIVATED, DEACTIVATED;

    public static AccountStatus fromString(String text) {

        return AccountStatus.valueOf(text.toUpperCase());
    }
}