package com.demo.emsp.domain.values;

import lombok.Data;

import java.util.Optional;

@Data
public class ContractId {
    private final String value;

    public ContractId(String value) {
//        if (value == null || value.isBlank()) {
//            throw new IllegalArgumentException("ContractId cannot be empty.");
//        }
        this.value = Optional.ofNullable(value)
                .filter(v -> !v.isEmpty())
                .orElseGet(() -> EMAID.generateEMAID());
    }

}
