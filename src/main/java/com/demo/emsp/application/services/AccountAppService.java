package com.demo.emsp.application.services;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.demo.emsp.application.dto.AccountDTO;
import com.demo.emsp.application.dto.TokenDTO;
import com.demo.emsp.domain.entity.Account;
import com.demo.emsp.domain.entity.Token;
import com.demo.emsp.domain.repository.AccountRepository;
import com.demo.emsp.domain.services.AccountDomainService;
import com.demo.emsp.domain.values.AccountId;
import com.demo.emsp.infrastructure.utils.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountAppService {
    @Autowired
    private AccountDomainService accountDomainService;

    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    public AccountDTO createAccount(AccountDTO accountDTO) {
        Account account = ConvertUtils.accountDtoToDomain(accountDTO);
        List<Token> tokenList = Optional.ofNullable(accountDTO.getTokens())
                .orElse(Collections.emptyList())
                .stream()
                .map(ConvertUtils::tokenDtoToDomain)
                .collect(Collectors.toList());

        account.setTokens(tokenList);
        Account accountSaved = accountDomainService.saveAccount(account);

        AccountDTO accountDTOResponse = ConvertUtils.accountDomainToDto(accountSaved);
        List<TokenDTO> tokenDTOList = Optional.ofNullable(accountSaved.getTokens())
                .orElse(Collections.emptyList())
                .stream()
                .map(ConvertUtils::tokenDomainToDto)
                .collect(Collectors.toList());
        accountDTOResponse.setTokens(tokenDTOList);
        return accountDTOResponse;
    }

    @Transactional
    public AccountDTO updateAccountStatus(AccountDTO accountDTO) {
        Account account = ConvertUtils.accountDtoToDomain(accountDTO);
        account = accountDomainService.updateAccountStatus(account);
        return ConvertUtils.accountDomainToDto(account);
    }

    @Transactional
    public AccountDTO getAccount(String id) {
        Account accountExisted = accountDomainService.getAccount(new AccountId(id));
        AccountDTO accountDTO = ConvertUtils.accountDomainToDto(accountExisted);

        List<TokenDTO> tokenDTOList = Optional.ofNullable(accountExisted.getTokens())
                .orElse(Collections.emptyList())
                .stream()
                .map(ConvertUtils::tokenDomainToDto)
                .collect(Collectors.toList());

        accountDTO.setTokens(tokenDTOList);
        return accountDTO;
    }

    @Transactional
    public IPage<AccountDTO> findAccountByLastUpdate(LocalDateTime startTime, LocalDateTime endTime, Integer page, Integer size) {
        IPage<Account> accountPage = accountRepository.findAccountByLastUpdate(startTime, endTime, page, size);
        return accountPage.convert(ConvertUtils::accountDomainToDto);
    }
}
