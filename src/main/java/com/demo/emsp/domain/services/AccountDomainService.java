package com.demo.emsp.domain.services;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.demo.emsp.domain.entity.Account;
import com.demo.emsp.domain.entity.Token;
import com.demo.emsp.domain.enums.AccountStatus;
import com.demo.emsp.domain.events.AssignedTokenEvent;
import com.demo.emsp.domain.events.AssignedTokenPublisher;
import com.demo.emsp.domain.repository.AccountRepository;
import com.demo.emsp.domain.repository.TokenRepository;
import com.demo.emsp.domain.values.AccountId;
import com.demo.emsp.domain.values.TokenId;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class AccountDomainService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Resource
    private AssignedTokenPublisher assignedTokenPublisher;

    @Autowired
    private ApplicationEventPublisher ventPublisher;

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

        Optional.ofNullable(accountExist.getAccountStatus())
                .filter(status -> status == AccountStatus.ACTIVATED)
                .orElseThrow(() -> new IllegalStateException("Token must be assign to ACTIVATED Account"));

        tokenExist.assignTo(new AccountId(accountExist.getId()));

        tokenRepository.update(tokenExist)
                .orElseThrow(() -> new RuntimeException("Token Update Failed"));

        Token tokenSaved = tokenRepository.findById(new TokenId(token.getId()))
                .orElseThrow(() -> new RuntimeException("Token Not existed"));

        accountExist.getTokens().add(tokenSaved);

        AssignedTokenEvent assignedTokenEvent = new AssignedTokenEvent(this,
                new TokenId(tokenSaved.getId()), new AccountId(accountExist.getId()));

        assignedTokenPublisher.publish(assignedTokenEvent);

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

    public IPage<Account> findAccountByLastUpdate(
            LocalDateTime startTime, LocalDateTime endTime, Integer page, Integer size) {
        return accountRepository.findAccountByLastUpdate(startTime, endTime, page, size);
    }

}
