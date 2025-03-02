package com.demo.emsp.application.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.demo.emsp.application.dto.AccountDTO;
import com.demo.emsp.application.services.AccountAppService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountAppService accountAppService;

    @PostMapping
    public ResponseEntity<AccountDTO> createAccount(@RequestBody @Valid AccountDTO accountDTO) {
        AccountDTO dto = accountAppService.createAccount(accountDTO);
        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AccountDTO> updateAccountStatus(@PathVariable String id, @RequestBody AccountDTO accountDTO) {
        accountDTO.setId(id);
        AccountDTO updatedDto = accountAppService.updateAccountStatus(accountDTO);
        return ResponseEntity.ok(updatedDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getAccount(@PathVariable String id) {
        AccountDTO accountDTO = accountAppService.getAccount(id);
        return ResponseEntity.ok(accountDTO);
    }

    @GetMapping("/search")
    public ResponseEntity<IPage<AccountDTO>> searchAccountByLastUpdated(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        IPage<AccountDTO> accountList = accountAppService.findAccountByLastUpdate(start, end, page, size);
        return ResponseEntity.ok(accountList);
    }

}
