package com.demo.emsp.domain.values;

import com.demo.emsp.domain.enums.TokenType;
import lombok.Data;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class ContractId {
    private final String value;
    private final TokenType type;

    private static final Random RANDOM = new Random();
    private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final String HEX_CHARS = "0123456789ABCDEF";

    public ContractId(String value, TokenType type) {
        this.type = type;
        validateValueByType(value, type);
        this.value = value;
    }

    private void validateValueByType(String value, TokenType type) {
        switch (type) {
            case EMAID -> validateEMAID(value);
            case RFID -> validateRFID(value);
            case MAC_ADDRESS -> validateMacAddress(value);
            default -> throw new IllegalArgumentException("Invalid TokenType");
        }
    }

    public static ContractId generate(TokenType type) {
        String generatedValue = switch (type) {
            case EMAID -> generateEMAID();
            case RFID -> generateRFID();
            case MAC_ADDRESS -> generateMacAddress();
            default -> throw new IllegalArgumentException("Unsupported TokenType");
        };
        return new ContractId(generatedValue, type);
    }

    private static String randomString(int length, String charPool) {
        return IntStream.range(0, length)
                .mapToObj(i -> String.valueOf(charPool.charAt(RANDOM.nextInt(charPool.length()))))
                .collect(Collectors.joining());
    }

    private static String generateEMAID() {
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

    private static String generateMacAddress() {
        String separator = RANDOM.nextBoolean() ? ":" : "-";
        return IntStream.range(0, 6)
                .mapToObj(i -> randomString(2, HEX_CHARS))
                .collect(Collectors.joining(separator));
    }

    private static String generateRFID() {
        return IntStream.range(0, 10)
                .mapToObj(i -> String.valueOf(RANDOM.nextInt(10)))
                .collect(Collectors.joining());
    }


    private void validateEMAID(String value) {
        String regex = "^[a-zA-Z]{2}(-?)[a-zA-Z0-9]{3}\\1[a-zA-Z0-9]{9}(\\1[a-zA-Z0-9])?$";
        if (!value.matches(regex)) {
            throw new IllegalArgumentException("Invalid EMAID format.");
        }
    }

    private void validateRFID(String value) {
        String regex = "^[0-9]+$";
        if (!value.matches(regex)) {
            throw new IllegalArgumentException("Invalid RFID format.");
        }
    }

    private void validateMacAddress(String value) {
        String regex = "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$";
        if (!value.matches(regex)) {
            throw new IllegalArgumentException("Invalid MAC address format.");
        }
    }

}
