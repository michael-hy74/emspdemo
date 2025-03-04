package com.demo.emsp.domain.values;

import lombok.Data;

import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class RFID {
    private final String value;

    private static final Random RANDOM = new Random();

    public RFID(String value) {
//        if (value == null || value.isBlank()) {
//            throw new IllegalArgumentException("AccountId cannot be empty.");
//        }
        this.value = Optional.ofNullable(value)
                .filter(v -> !v.isEmpty())
                .orElseGet(() -> generateRFID());
    }

    public static void validateRFID(String value) {
        String regex = "^[0-9]+$";
        if (!value.matches(regex)) {
            throw new IllegalArgumentException("Invalid RFID format.");
        }
    }

    public static String generateRFID() {
        return IntStream.range(0, 10)
                .mapToObj(i -> String.valueOf(RANDOM.nextInt(10)))
                .collect(Collectors.joining());
    }

}
