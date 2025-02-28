package com.demo.emsp.domain.repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.demo.emsp.domain.entity.Account;
import com.demo.emsp.domain.entity.Token;
import com.demo.emsp.domain.values.AccountId;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AccountRepository {

    Optional<Account> save(Account account);

    Optional<Account> update(Account account);

    Optional<Account> findById(AccountId id);

    Optional<List<Token>> findTokensByAccountId(AccountId id);

    IPage<Account> findAccountByLastUpdate(LocalDateTime startTime, LocalDateTime endTime, Integer page, Integer size);

}
