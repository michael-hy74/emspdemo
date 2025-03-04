package com.demo.emsp.domain.services;

import com.demo.emsp.domain.entity.Account;
import com.demo.emsp.domain.entity.Token;
import com.demo.emsp.domain.enums.AccountStatus;
import com.demo.emsp.domain.enums.TokenStatus;
import com.demo.emsp.domain.repository.AccountRepository;
import com.demo.emsp.domain.repository.TokenRepository;
import com.demo.emsp.domain.values.AccountId;
import com.demo.emsp.domain.values.TokenId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class AccountDomainService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TokenRepository tokenRepository;

    public Account saveAccount(Account account) {
        Account accountSaved = accountRepository.save(account.checkAndGenerateContractId(account))
                .orElseThrow(() -> new RuntimeException("Account saved failed"));
        return accountSaved;
    }

    public Account updateAccountStatus(Account account) {
        Account accountExist = accountRepository.findById(new AccountId(account.getId()))
                .orElseThrow(() -> new RuntimeException("Account Not existed"));
        switch (account.getAccountStatus()) {
            case ACTIVATED:
                accountExist.activate();
                break;
            case DEACTIVATED:
                accountExist.deactivate();
                break;
            default:
                throw new IllegalArgumentException("Invalid account status: " + account.getAccountStatus());
        }
        accountRepository.update(accountExist)
                .orElseThrow(() -> new RuntimeException("Account Update Failed"));

        Account accountGetNew = accountRepository.findById(new AccountId(account.getId()))
                .orElseThrow(() -> new RuntimeException("Account Get Failed"));
        return accountGetNew;
    }

    public Account assignToken(Token token) {
        Token tokenExist = tokenRepository.findById(new TokenId(token.getId()))
                .orElseThrow(() -> new RuntimeException("Token Not existed"));

        Account accountExist = getAccount(token.getAccountId());

        if (accountExist.getAccountStatus() != AccountStatus.ACTIVATED) {
            throw new IllegalStateException("Token must be assign to ACTIVATED Account");
        }

        tokenExist.setAccountId(token.getAccountId());
        tokenExist.setTokenStatus(TokenStatus.ASSIGNED);
        tokenRepository.update(tokenExist)
                .orElseThrow(() -> new RuntimeException("Token Update Failed"));

        Token tokenSaved = tokenRepository.findById(new TokenId(token.getId()))
                .orElseThrow(() -> new RuntimeException("Token Not existed"));

        accountExist.getTokens().add(tokenSaved);
        return accountExist;
    }

    public Account getAccount(AccountId accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account Not existed"));

        List<Token> tokens = accountRepository.findTokensByAccountId(accountId)
                .orElse(new ArrayList<>());
        account.setTokens(tokens);
        return account;
    }

}
