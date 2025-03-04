package com.demo.emsp.application.services;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.demo.emsp.domain.entity.Token;
import com.demo.emsp.domain.repository.TokenRepository;
import com.demo.emsp.domain.services.TokenDomainService;
import com.demo.emsp.domain.values.TokenId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class TokenAppService {

    @Autowired
    private TokenDomainService tokenDomainService;

    @Autowired
    private TokenRepository tokenRepository;

    @Transactional
    public Token createToken(Token token) {
        return tokenDomainService.saveToken(token);
    }

    @Transactional
    public Token updateTokenStatus(Token token) {
        return tokenDomainService.updateTokenStatus(token);
    }

    @Transactional
    public Token getToken(String tokenId) {
        return tokenDomainService.getToken(new TokenId(tokenId));
    }

    @Transactional
    public IPage<Token> findTokenByLastUpdate(LocalDateTime startTime, LocalDateTime endTime, Integer page, Integer size) {
        return tokenRepository.findTokenByLastUpdate(startTime, endTime, page, size);
    }
}
