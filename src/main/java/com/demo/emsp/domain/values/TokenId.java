package com.demo.emsp.domain.values;

import lombok.Data;

@Data
public class TokenId {
    private final String value;

    public TokenId(String value) {
//        if (value == null || value.isBlank()) {
//            throw new IllegalArgumentException("AccountId cannot be empty.");
//        }
        this.value = value;
    }

}
