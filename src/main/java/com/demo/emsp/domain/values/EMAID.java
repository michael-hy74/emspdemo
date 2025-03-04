package com.demo.emsp.domain.values;

import lombok.Data;

import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class EMAID {
    private final String value;

    private static final Random RANDOM = new Random();
    private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public EMAID(String value) {
//        if (value == null || value.isBlank()) {
//            throw new IllegalArgumentException("EMAID cannot be empty.");
//        }
        this.value = Optional.ofNullable(value)
                .filter(v -> !v.isEmpty())
                .orElseGet(() -> generateEMAID());
    }

    public static void validateEMAID(String value) {
        String regex = "^[a-zA-Z]{2}(-?)[a-zA-Z0-9]{3}\\1[a-zA-Z0-9]{9}(\\1[a-zA-Z0-9])?$";
        if (!value.matches(regex)) {
            throw new IllegalArgumentException("Invalid EMAID format.");
        }
    }

    public static String generateEMAID() {
        boolean useSeparator = RANDOM.nextBoolean();
        String separator = useSeparator ? "-" : "";

        String part1 = randomString(2, ALPHANUMERIC).toUpperCase();
        String part2 = randomString(3, ALPHANUMERIC);
        String part3 = randomString(9, ALPHANUMERIC);
        String part4 = RANDOM.nextBoolean() ? randomString(1, ALPHANUMERIC) : "";

        return String.join(separator,
                part1,
                part2 + separator + part3 +
                        (part4.isEmpty() ? "" : separator + part4)
        ).replaceAll("--", "-");
    }

    private static String randomString(int length, String charPool) {
        return IntStream.range(0, length)
                .mapToObj(i -> String.valueOf(charPool.charAt(RANDOM.nextInt(charPool.length()))))
                .collect(Collectors.joining());
    }

}
