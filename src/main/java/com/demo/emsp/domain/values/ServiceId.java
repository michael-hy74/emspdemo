package com.demo.emsp.domain.values;

import lombok.Data;

@Data
public final class ServiceId {
    private final String value;

    public ServiceId(String value) {
//        if (value == null || value.isBlank()) {
//            throw new IllegalArgumentException("ServiceID cannot be empty.");
//        }
        this.value = value;
    }
}
