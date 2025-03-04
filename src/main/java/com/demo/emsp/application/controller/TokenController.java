package com.demo.emsp.application.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.demo.emsp.application.dto.TokenDTO;
import com.demo.emsp.application.services.TokenAppService;
import com.demo.emsp.domain.entity.Token;
import com.demo.emsp.infrastructure.utils.ConvertUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/tokens")
public class TokenController {

    @Autowired
    private TokenAppService tokenAppService;

    @PostMapping
    @Validated
    public ResponseEntity<TokenDTO> createToken(@RequestBody @Valid TokenDTO tokenDTO) {
        Token token = ConvertUtils.tokenDtoToDomain(tokenDTO);
        token = tokenAppService.createToken(token);
        return ResponseEntity.ok(ConvertUtils.tokenDomainToDto(token));
    }

    @PatchMapping("/{tokenId}")
    public ResponseEntity<TokenDTO> updateTokenStatus(@PathVariable String tokenId, @RequestBody TokenDTO tokenDTO) {
        tokenDTO.setId(tokenId);
        Token token = ConvertUtils.tokenDtoToDomain(tokenDTO);
        token = tokenAppService.updateTokenStatus(token);
        return ResponseEntity.ok(ConvertUtils.tokenDomainToDto(token));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TokenDTO> getToken(@PathVariable String id) {
        Token token = tokenAppService.getToken(id);
        return ResponseEntity.ok(ConvertUtils.tokenDomainToDto(token));
    }

    @GetMapping("/search")
    public ResponseEntity<IPage<TokenDTO>> searchTokenByLastUpdated(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        IPage<Token> tokenPage = tokenAppService.findTokenByLastUpdate(start, end, page, size);
        return ResponseEntity.ok(tokenPage.convert(ConvertUtils::tokenDomainToDto));
    }
}
