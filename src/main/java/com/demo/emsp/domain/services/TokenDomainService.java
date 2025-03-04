package com.demo.emsp.domain.services;

import com.demo.emsp.domain.entity.Token;
import com.demo.emsp.domain.repository.TokenRepository;
import com.demo.emsp.domain.values.TokenId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenDomainService {

    @Autowired
    private TokenRepository tokenRepository;

    public Token saveToken(Token token) {
        token.setValue(token.generateTokenByType(token.getTokenType()));
        Token tokenSaved = tokenRepository.save(token)
                .orElseThrow(() -> new RuntimeException("Account saved failed"));
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

}
