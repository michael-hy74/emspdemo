package com.demo.emsp.domain.services;

import com.demo.emsp.domain.entity.Account;
import com.demo.emsp.domain.entity.Token;
import com.demo.emsp.domain.enums.AccountStatus;
import com.demo.emsp.domain.enums.TokenStatus;
import com.demo.emsp.domain.repository.AccountRepository;
import com.demo.emsp.domain.repository.TokenRepository;
import com.demo.emsp.domain.values.AccountId;
import com.demo.emsp.domain.values.ContractId;
import com.demo.emsp.domain.values.TokenId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
public class AccountDomainService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TokenRepository tokenRepository;

    public Account saveAccount(Account account) {
        Account accountSaved = accountRepository.save(account)
                .orElseThrow(() -> new RuntimeException("Account saved failed"));

        AccountId accountIdExisted = new AccountId(accountSaved.getId());

        accountSaved = accountRepository.findById(accountIdExisted)
                .orElseThrow(() -> new RuntimeException("Get exist Account failed"));

        Optional.ofNullable(account.getTokens())
                .orElse(Collections.emptyList())
                .forEach(token -> {
                    token.setAccountId(accountIdExisted);
                    token.setContractId(ContractId.generate(token.getTokenType()));
                    Optional<Token> tokenSaved = tokenRepository.save(token);
                    tokenSaved.orElseThrow(() -> new RuntimeException("Token saved failed"));
                });

        List<Token> tokenSavedList = accountRepository.findTokensByAccountId(accountIdExisted)
                .orElse(new ArrayList<>());
        accountSaved.setTokens(tokenSavedList);
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

    public Token updateTokenStatus(Token token) {
        Token tokenExist = tokenRepository.findById(new TokenId(token.getId()))
                .orElseThrow(() -> new RuntimeException("Token Not existed"));

        switch (token.getTokenStatus()) {
            case ACTIVATED:
                tokenExist.activate();
                break;
            case DEACTIVATED:
                tokenExist.deactivate();
                break;
            default:
                throw new IllegalArgumentException("Invalid token status: " + token.getTokenStatus());
        }
        tokenRepository.update(tokenExist)
                .orElseThrow(() -> new RuntimeException("Token Update Failed"));

        Token tokenGetNew = tokenRepository.findById(new TokenId(token.getId()))
                .orElseThrow(() -> new RuntimeException("Token Get Failed"));
        return tokenGetNew;
    }

    public Token assignToken(Token token) {
        Token tokenExist = tokenRepository.findById(new TokenId(token.getId()))
                .orElseThrow(() -> new RuntimeException("Token Not existed"));

        Account accountExist = accountRepository.findById(token.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account Not existed"));

        if (accountExist.getAccountStatus() != AccountStatus.ACTIVATED) {
            throw new IllegalStateException("Token must be assign to ACTIVATED Account.");
        }

        tokenExist.setAccountId(token.getAccountId());
        tokenExist.setTokenStatus(TokenStatus.ASSIGNED);
        tokenRepository.assignToken(tokenExist)
                .orElseThrow(() -> new RuntimeException("Token Update Failed"));

        Token tokenGetNew = tokenRepository.findById(new TokenId(token.getId()))
                .orElseThrow(() -> new RuntimeException("Token Get Failed"));

        return tokenGetNew;
    }

    public Account getAccount(AccountId accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        List<Token> tokens = accountRepository.findTokensByAccountId(accountId)
                .orElse(Collections.emptyList());
        account.setTokens(tokens);
        return account;
    }

    public Token getToken(TokenId tokenId) {
        Token token = tokenRepository.findById(tokenId)
                .orElseThrow(() -> new RuntimeException("Token not found"));
        return token;
    }
}
