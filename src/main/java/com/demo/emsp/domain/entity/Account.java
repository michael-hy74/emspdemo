package com.demo.emsp.domain.entity;

import com.demo.emsp.domain.enums.AccountStatus;
import com.demo.emsp.domain.values.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/***
 * Account Entity -- root
 */
@Data
public class Account {
    private String id;
    private ServiceId serviceId;
    private FleetSolution fleetSolution;
    private ContractId contractId;
    private AccountStatus accountStatus;
    private LocalDateTime createdDate;
    private LocalDateTime lastUpdated;
    private List<Token> tokens;

    public void activate() {
        if (this.accountStatus != AccountStatus.CREATED) {
            throw new IllegalStateException("Account must be CREATED to activate.");
        }
        this.accountStatus = AccountStatus.ACTIVATED;
        this.lastUpdated = LocalDateTime.now();
    }

    public void deactivate() {
        if (this.accountStatus != AccountStatus.ACTIVATED) {
            throw new IllegalStateException("Account must be ACTIVATED to deactivate.");
        }
        this.accountStatus = AccountStatus.DEACTIVATED;
        this.lastUpdated = LocalDateTime.now();
    }

    public Account checkAndGenerateContractId(Account account) {
        account.setContractId(Optional.ofNullable(account.getContractId())
                .map(ContractId::getValue)
                .filter(value -> value != null && !value.isEmpty())
                .map(value -> new ContractId(value))
                .orElseGet(() -> new ContractId(EMAID.generateEMAID())));
        return account;
    }
}

