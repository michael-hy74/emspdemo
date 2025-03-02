package com.demo.emsp.infrastructure.repositoryImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.emsp.domain.entity.Account;
import com.demo.emsp.domain.entity.Token;
import com.demo.emsp.domain.repository.AccountRepository;
import com.demo.emsp.domain.values.AccountId;
import com.demo.emsp.infrastructure.mapper.AccountPOMapper;
import com.demo.emsp.infrastructure.po.AccountPO;
import com.demo.emsp.infrastructure.po.TokenPO;
import com.demo.emsp.infrastructure.services.AccountPOService;
import com.demo.emsp.infrastructure.services.TokenPOService;
import com.demo.emsp.infrastructure.utils.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class AccountRepositoryImpl implements AccountRepository {

    @Autowired
    AccountPOService accountPOServices;

    @Autowired
    AccountPOMapper accountPOMapper;

    @Autowired
    TokenPOService tokenPOService;

    @Override
    public Optional<Account> save(Account account) {
        AccountPO accountPO = ConvertUtils.accountDomainToPo(account);
        boolean accountSaved = accountPOServices.save(accountPO);
        return accountSaved ? Optional.of(ConvertUtils.accountPOToDomain(accountPO)) : Optional.empty();
    }

    @Override
    public Optional<Account> update(Account account) {
        AccountPO accountPO = ConvertUtils.accountDomainToPo(account);
        LambdaQueryWrapper<AccountPO> query = new LambdaQueryWrapper();
        query.eq(AccountPO::getId, account.getId());
        boolean accountSaved = accountPOServices.update(accountPO, query);
        return accountSaved ? Optional.of(ConvertUtils.accountPOToDomain(accountPO)) : Optional.empty();
    }

    @Override
    public Optional<Account> findById(AccountId id) {
        AccountPO accountPO = accountPOServices.getById(id.getValue());
        Account account = ConvertUtils.accountPOToDomain(accountPO);
        return Optional.of(account);
    }

    @Override
    public Optional<List<Token>> findTokensByAccountId(AccountId id) {
        LambdaQueryWrapper<TokenPO> query = new LambdaQueryWrapper();
        query.eq(TokenPO::getAccountId, id.getValue());

        return Optional.ofNullable(tokenPOService.list(query))
                .filter(list -> !list.isEmpty())
                .map(list -> list.stream()
                    .map(ConvertUtils::tokenPOToDomain)
                    .collect(Collectors.toList()));
    }

    @Override
    public IPage<Account> findAccountByLastUpdate(LocalDateTime startTime, LocalDateTime endTime, Integer page, Integer size) {
        int pageNum = Optional.ofNullable(page).filter(p -> p > 0).orElse(1);
        int pageSize = Optional.ofNullable(size).filter(s -> s > 0).orElse(10);
        Page<AccountPO> accountPOPage = new Page<>(pageNum, pageSize);

        QueryWrapper<AccountPO> queryWrapper = new QueryWrapper<>();
        Optional.ofNullable(startTime).ifPresent(start -> queryWrapper.ge("last_updated", start));
        Optional.ofNullable(endTime).ifPresent(end -> queryWrapper.le("last_updated", end));

        Page<AccountPO> poPage = accountPOMapper.selectPage(accountPOPage, queryWrapper);
        return poPage.convert(ConvertUtils::accountPOToDomain);
    }


}
