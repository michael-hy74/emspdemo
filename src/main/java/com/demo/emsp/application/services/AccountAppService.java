package com.demo.emsp.application.services;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.demo.emsp.domain.entity.Account;
import com.demo.emsp.domain.entity.Token;
import com.demo.emsp.domain.repository.AccountRepository;
import com.demo.emsp.domain.services.AccountDomainService;
import com.demo.emsp.domain.values.AccountId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AccountAppService {
    @Autowired
    private AccountDomainService accountDomainService;

    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    public Account createAccount(Account account) {
        return accountDomainService.saveAccount(account);
    }

    @Transactional
    public Account updateAccountStatus(Account account) {
        return accountDomainService.updateAccountStatus(account);
    }

    @Transactional
    public Account getAccount(String id) {
        return accountDomainService.getAccount(new AccountId(id));
    }

    @Transactional
    public Account assignToken(Token token) {
        return accountDomainService.assignToken(token);
    }

    @Transactional
    public IPage<Account> findAccountByLastUpdate(LocalDateTime startTime, LocalDateTime endTime, Integer page, Integer size) {
        return accountRepository.findAccountByLastUpdate(startTime, endTime, page, size);
    }
}
