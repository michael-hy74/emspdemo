package com.demo.emsp.application.services;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.demo.emsp.application.dto.TokenDTO;
import com.demo.emsp.domain.entity.Token;
import com.demo.emsp.domain.repository.TokenRepository;
import com.demo.emsp.domain.services.AccountDomainService;
import com.demo.emsp.domain.values.ContractId;
import com.demo.emsp.domain.values.TokenId;
import com.demo.emsp.infrastructure.utils.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class TokenAppService {

    @Autowired
    private AccountDomainService accountDomainService;

    @Autowired
    private TokenRepository tokenRepository;

    @Transactional
    public TokenDTO createToken(TokenDTO tokenDTO) {
        Token token = ConvertUtils.tokenDtoToDomain(tokenDTO);
        token.setContractId(ContractId.generate(token.getTokenType()));
        token = tokenRepository.save(token).orElse(new Token());
        return ConvertUtils.tokenDomainToDto(token);
    }

    @Transactional
    public TokenDTO updateTokenStatus(TokenDTO tokenDTO) {
        Token token = ConvertUtils.tokenDtoToDomain(tokenDTO);
        token = accountDomainService.updateTokenStatus(token);
        return ConvertUtils.tokenDomainToDto(token);
    }

    @Transactional
    public TokenDTO assignToken(TokenDTO tokenDTO) {
        Token token = ConvertUtils.tokenDtoToDomain(tokenDTO);
        token.setAssignedDate(LocalDateTime.now());
        token = accountDomainService.assignToken(token);
        return ConvertUtils.tokenDomainToDto(token);
    }

    @Transactional
    public TokenDTO getToken(String tokenId) {
        Token token = accountDomainService.getToken(new TokenId(tokenId));
        return ConvertUtils.tokenDomainToDto(token);
    }

    @Transactional
    public IPage<TokenDTO> findTokenByLastUpdate(LocalDateTime startTime, LocalDateTime endTime, Integer page, Integer size) {
        IPage<Token> accountPage = tokenRepository.findTokenByLastUpdate(startTime, endTime, page, size);
        return accountPage.convert(ConvertUtils::tokenDomainToDto);
    }
}
