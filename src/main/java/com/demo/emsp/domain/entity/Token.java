package com.demo.emsp.domain.entity;

import com.demo.emsp.domain.enums.TokenStatus;
import com.demo.emsp.domain.enums.TokenType;
import com.demo.emsp.domain.values.AccountId;
import com.demo.emsp.domain.values.ContractId;
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
    private ContractId contractId;
    private AccountId accountId;
    private LocalDateTime createdDate;
    private LocalDateTime assignedDate;
    private LocalDateTime lastUpdated;

    public void assignTo(String accountId) {
        if (this.tokenStatus != TokenStatus.CREATED) {
            throw new IllegalStateException("Token must be CREATED to ASSIGNED.");
        }
        this.accountId = new AccountId(accountId);
        this.tokenStatus = TokenStatus.ASSIGNED;
        this.assignedDate = LocalDateTime.now();
    }

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
}
