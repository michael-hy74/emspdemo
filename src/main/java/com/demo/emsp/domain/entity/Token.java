package com.demo.emsp.domain.entity;

import com.demo.emsp.domain.enums.TokenStatus;
import com.demo.emsp.domain.enums.TokenType;
import com.demo.emsp.domain.values.*;
import lombok.Data;

import java.time.LocalDateTime;


/***
 * Token Entity
 */
@Data
public class Token {
    private String id;
    private TokenType tokenType;
    private TokenStatus tokenStatus;
    private String value;
    private AccountId accountId;
    private LocalDateTime createdDate;
    private LocalDateTime assignedDate;
    private LocalDateTime lastUpdated;

    public void activate() {
        if (this.tokenStatus != TokenStatus.ASSIGNED) {
            throw new IllegalStateException("Token must be ASSIGNED to ACTIVATED.");
        }
        this.tokenStatus = TokenStatus.ACTIVATED;
        this.lastUpdated = LocalDateTime.now();
    }

    public void deactivate() {
        if (this.tokenStatus != TokenStatus.ACTIVATED) {
            throw new IllegalStateException("Token must be ACTIVATED to deactivate.");
        }
        this.tokenStatus = TokenStatus.DEACTIVATED;
        this.lastUpdated = LocalDateTime.now();
    }

    private void validateTokenByType(String value, TokenType type) {
        switch (type) {
            case EMAID -> EMAID.validateEMAID(value);
            case RFID -> RFID.validateRFID(value);
            case MAC_ADDRESS -> MacAddress.validateMacAddress(value);
            default -> throw new IllegalArgumentException("Invalid TokenType");
        }
    }

    public static String generateTokenByType(TokenType type) {
        String generatedValue = switch (type) {
            case EMAID -> EMAID.generateEMAID();
            case RFID -> RFID.generateRFID();
            case MAC_ADDRESS -> MacAddress.generateMacAddress();
            default -> throw new IllegalArgumentException("Unsupported TokenType");
        };
        return generatedValue;
    }
}
