package com.demo.emsp.infrastructure.repositoryImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.emsp.domain.entity.Token;
import com.demo.emsp.domain.repository.TokenRepository;
import com.demo.emsp.domain.values.TokenId;
import com.demo.emsp.infrastructure.mapper.TokenPOMapper;
import com.demo.emsp.infrastructure.po.TokenPO;
import com.demo.emsp.infrastructure.services.TokenPOService;
import com.demo.emsp.infrastructure.utils.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public class TokenRepositoryImpl implements TokenRepository {

    @Autowired
    TokenPOService tokenPOService;

    @Autowired
    TokenPOMapper tokenPOMapper;

    @Override
    public Optional<Token> save(Token token) {
        TokenPO tokenPO = ConvertUtils.tokenDomainToPo(token);
        boolean tokenSaved = tokenPOService.save(tokenPO);
        tokenPO = tokenPOService.getById(tokenPO.getId());
        return tokenSaved ? Optional.of(ConvertUtils.tokenPOToDomain(tokenPO)) : Optional.empty();
    }

    @Override
    public Optional<Token> update(Token token) {
        TokenPO tokenPO = ConvertUtils.tokenDomainToPo(token);
        LambdaQueryWrapper<TokenPO> query = new LambdaQueryWrapper();
        query.eq(TokenPO::getId, token.getId());
        boolean tokenSaved = tokenPOService.update(tokenPO, query);
        return tokenSaved ? Optional.of(ConvertUtils.tokenPOToDomain(tokenPO)) : Optional.empty();
    }

    @Override
    public Optional<Token> findById(TokenId id) {
        TokenPO tokenPO = tokenPOService.getById(id.getValue());
        return Optional.of(ConvertUtils.tokenPOToDomain(tokenPO));
    }

    @Override
    public IPage<Token> findTokenByLastUpdate(LocalDateTime startTime, LocalDateTime endTime, Integer page, Integer size) {
        int pageNum = Optional.ofNullable(page).filter(p -> p > 0).orElse(1);
        int pageSize = Optional.ofNullable(size).filter(s -> s > 0).orElse(10);
        Page<TokenPO> tokenPOPage = new Page<>(pageNum, pageSize);

        QueryWrapper<TokenPO> queryWrapper = new QueryWrapper<>();
        Optional.ofNullable(startTime).ifPresent(start -> queryWrapper.ge("last_updated", start));
        Optional.ofNullable(endTime).ifPresent(end -> queryWrapper.le("last_updated", end));

        Page<TokenPO> poPage = tokenPOMapper.selectPage(tokenPOPage, queryWrapper);
        return poPage.convert(ConvertUtils::tokenPOToDomain);
    }

}
