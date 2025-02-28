package com.demo.emsp.application.services;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.demo.emsp.application.dto.AccountDTO;
import com.demo.emsp.domain.entity.Account;
import com.demo.emsp.domain.repository.AccountRepository;
import com.demo.emsp.domain.services.AccountDomainService;
import com.demo.emsp.domain.values.AccountId;
import com.demo.emsp.infrastructure.utils.ConvertUtils;
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
    public AccountDTO createAccount(AccountDTO accountDTO) {
        Account account = ConvertUtils.accountDtoToDomain(accountDTO);
        account = accountDomainService.saveAccount(account);
        return ConvertUtils.accountDomainToDto(account);
    }

    @Transactional
    public AccountDTO updateAccountStatus(AccountDTO accountDTO) {
        Account account = ConvertUtils.accountDtoToDomain(accountDTO);
        account = accountDomainService.updateAccountStatus(account);
        return ConvertUtils.accountDomainToDto(account);
    }

    @Transactional
    public AccountDTO getAccount(String id) {
        Account account = accountDomainService.getAccount(new AccountId(id));
        return ConvertUtils.accountDomainToDto(account);
    }

    @Transactional
    public IPage<AccountDTO> findAccountByLastUpdate(LocalDateTime startTime, LocalDateTime endTime, Integer page, Integer size) {
        IPage<Account> accountPage = accountRepository.findAccountByLastUpdate(startTime, endTime, page, size);
        return accountPage.convert(ConvertUtils::accountDomainToDto);
    }
}
