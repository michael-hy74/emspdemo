package com.demo.emsp.domain.values;

import lombok.Data;

import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class MacAddress {
    private final String value;

    private static final Random RANDOM = new Random();
    private static final String HEX_CHARS = "0123456789ABCDEF";

    public MacAddress(String value) {
//        if (value == null || value.isBlank()) {
//            throw new IllegalArgumentException("Mac-Address cannot be empty.");
//        }
        this.value = Optional.ofNullable(value)
                .filter(v -> !v.isEmpty())
                .orElseGet(() -> generateMacAddress());
    }

    public static void validateMacAddress(String value) {
        String regex = "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$";
        if (!value.matches(regex)) {
            throw new IllegalArgumentException("Invalid MAC address format.");
        }
    }

    public static String generateMacAddress() {
        String separator = RANDOM.nextBoolean() ? ":" : "-";
        return IntStream.range(0, 6)
                .mapToObj(i -> randomString(2, HEX_CHARS))
                .collect(Collectors.joining(separator));
    }

    private static String randomString(int length, String charPool) {
        return IntStream.range(0, length)
                .mapToObj(i -> String.valueOf(charPool.charAt(RANDOM.nextInt(charPool.length()))))
                .collect(Collectors.joining());
    }

}
