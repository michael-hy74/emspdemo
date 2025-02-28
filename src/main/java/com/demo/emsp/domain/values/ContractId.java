package com.demo.emsp.domain.values;

import com.demo.emsp.domain.enums.TokenType;
import lombok.Data;

@Data
public class ContractId {
    private final String value;
    private final TokenType type;

    public ContractId(String value, TokenType type) {
        this.type = type;
        switch (type) {
            case EMAID:
                validateEMAID(value);
                break;
            case RFID:
                validateRFID(value);
                break;
            case MAC_ADDRESS:
                validateMacAddress(value);
                break;
        }
        this.value = value;
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
