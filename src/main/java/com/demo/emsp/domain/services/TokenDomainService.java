package com.demo.emsp.domain.services;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.demo.emsp.domain.entity.Token;
import com.demo.emsp.domain.repository.TokenRepository;
import com.demo.emsp.domain.values.TokenId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TokenDomainService {

    @Autowired
    private TokenRepository tokenRepository;

    public Token saveToken(Token token) {
        token.setValue(token.generateTokenByType(token.getTokenType()));
        Token tokenSaved = tokenRepository.save(token)
                .orElseThrow(() -> new RuntimeException("Token saved failed"));
        return tokenSaved;
    }

    public Token updateTokenStatus(Token token) {
        Token tokenExist = tokenRepository.findById(new TokenId(token.getId()))
                .orElseThrow(() -> new RuntimeException("Token Not existed"));

        switch (token.getTokenStatus()) {
            case ACTIVATED:
                tokenExist.activate();
                break;
            case DEACTIVATED:
                tokenExist.deactivate();
                break;
            default:
                throw new IllegalArgumentException("Invalid token status: " + token.getTokenStatus());
        }
        tokenRepository.update(tokenExist)
                .orElseThrow(() -> new RuntimeException("Token Update Failed"));

        Token tokenGetNew = tokenRepository.findById(new TokenId(token.getId()))
                .orElseThrow(() -> new RuntimeException("Token Get Failed"));
        return tokenGetNew;
    }

    public Token getToken(TokenId tokenId) {
        Token token = tokenRepository.findById(tokenId)
                .orElseThrow(() -> new RuntimeException("Token not found"));
        return token;
    }

    public IPage<Token> findTokenByLastUpdate(
            LocalDateTime startTime, LocalDateTime endTime, Integer page, Integer size) {
        return tokenRepository.findTokenByLastUpdate(startTime, endTime, page, size);
    }

}
