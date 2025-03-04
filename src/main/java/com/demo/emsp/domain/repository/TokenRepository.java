package com.demo.emsp.domain.repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.demo.emsp.domain.entity.Token;
import com.demo.emsp.domain.values.TokenId;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TokenRepository {

    Optional<Token> save(Token token);

    Optional<Token> update(Token token);

    Optional<Token> findById(TokenId id);

    IPage<Token> findTokenByLastUpdate(LocalDateTime startTime, LocalDateTime endTime, Integer page, Integer size);


}
