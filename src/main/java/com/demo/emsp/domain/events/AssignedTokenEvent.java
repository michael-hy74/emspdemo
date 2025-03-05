package com.demo.emsp.domain.events;

import com.demo.emsp.domain.values.AccountId;
import com.demo.emsp.domain.values.TokenId;
import org.springframework.context.ApplicationEvent;

public class AssignedTokenEvent extends ApplicationEvent {

    private final TokenId tokenId;
    private final AccountId accountId;

    public AssignedTokenEvent(Object source, TokenId tokenId, AccountId accountId) {
        super(source);
        this.tokenId = tokenId;
        this.accountId = accountId;
    }

    public TokenId getTokenId() {
        return tokenId;
    }

    public AccountId getAccountId() {
        return accountId;
    }
}
