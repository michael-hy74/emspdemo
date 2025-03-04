package com.demo.emsp.application.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.demo.emsp.application.dto.AccountDTO;
import com.demo.emsp.application.dto.TokenDTO;
import com.demo.emsp.application.services.AccountAppService;
import com.demo.emsp.domain.entity.Account;
import com.demo.emsp.domain.entity.Token;
import com.demo.emsp.infrastructure.utils.ConvertUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountAppService accountAppService;


    @PostMapping
    public ResponseEntity<AccountDTO> createAccount(@RequestBody @Valid AccountDTO accountDTO) {
        Account account = ConvertUtils.accountDtoToDomain(accountDTO);
        account = accountAppService.createAccount(account);
        return ResponseEntity.ok(ConvertUtils.accountDomainToDto(account));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AccountDTO> updateAccountStatus(@PathVariable String id, @RequestBody AccountDTO accountDTO) {
        accountDTO.setId(id);
        Account account = ConvertUtils.accountDtoToDomain(accountDTO);
        account = accountAppService.updateAccountStatus(account);
        return ResponseEntity.ok(ConvertUtils.accountDomainToDto(account));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getAccount(@PathVariable String id) {
        Account accountExisted = accountAppService.getAccount(id);
        AccountDTO accountDTO = ConvertUtils.accountDomainToDto(accountExisted);

        List<TokenDTO> tokenDTOList = Optional.ofNullable(accountExisted.getTokens())
                .orElse(Collections.emptyList())
                .stream()
                .map(ConvertUtils::tokenDomainToDto)
                .collect(Collectors.toList());
        accountDTO.setTokens(tokenDTOList);
        return ResponseEntity.ok(accountDTO);
    }

    @PatchMapping("/assign/{accountId}")
    public ResponseEntity<AccountDTO> assignToken(@PathVariable String accountId, @RequestBody TokenDTO tokenDTO) {
        tokenDTO.setAccountId(accountId);
        Token token = ConvertUtils.tokenDtoToDomain(tokenDTO);
        Account accountSaved = accountAppService.assignToken(token);

        AccountDTO accountDTO = ConvertUtils.accountDomainToDto(accountSaved);

        List<TokenDTO> tokenDTOList = Optional.ofNullable(accountSaved.getTokens())
                .orElse(Collections.emptyList())
                .stream()
                .map(ConvertUtils::tokenDomainToDto)
                .collect(Collectors.toList());
        accountDTO.setTokens(tokenDTOList);
        return ResponseEntity.ok(accountDTO);
    }

    @GetMapping("/search")
    public ResponseEntity<IPage<AccountDTO>> searchAccountByLastUpdated(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        IPage<Account> accountPage = accountAppService.findAccountByLastUpdate(start, end, page, size);
        return ResponseEntity.ok(accountPage.convert(ConvertUtils::accountDomainToDto));
    }

}
