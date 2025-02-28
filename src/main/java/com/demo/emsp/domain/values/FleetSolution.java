package com.demo.emsp.domain.values;

import lombok.Data;

@Data
public class FleetSolution {
    private final String value;

    public FleetSolution(String value) {
//        if (value == null || value.isBlank()) {
//            throw new IllegalArgumentException("FleetSolution cannot be empty.");
//        }
        this.value = value;
    }
}
