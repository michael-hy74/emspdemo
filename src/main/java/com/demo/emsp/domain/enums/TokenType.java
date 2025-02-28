package com.demo.emsp.domain.enums;

public enum TokenType {
    RFID, EMAID, MAC_ADDRESS;

    public static TokenType fromString(String text) {
        return TokenType.valueOf(text.toUpperCase());
    }
}