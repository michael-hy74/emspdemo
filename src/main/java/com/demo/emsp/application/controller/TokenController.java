package com.demo.emsp.application.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.demo.emsp.application.dto.TokenDTO;
import com.demo.emsp.application.services.TokenAppService;
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
        TokenDTO dto = tokenAppService.createToken(tokenDTO);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{tokenId}")
    public ResponseEntity<TokenDTO> updateTokenStatus(@PathVariable String tokenId, @RequestBody TokenDTO tokenDTO) {
        tokenDTO.setId(tokenId);
        TokenDTO updateDto = tokenAppService.updateTokenStatus(tokenDTO);
        return ResponseEntity.ok(updateDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TokenDTO> getToken(@PathVariable String id) {
        TokenDTO tokenDTO = tokenAppService.getToken(id);
        return ResponseEntity.ok(tokenDTO);
    }

    @PutMapping("/assign/{tokenId}")
    public ResponseEntity<TokenDTO> assignToken(@PathVariable String tokenId, @RequestBody TokenDTO tokenDTO) {
        tokenDTO.setId(tokenId);
        TokenDTO updateDto = tokenAppService.assignToken(tokenDTO);
        return ResponseEntity.ok(updateDto);
    }

    @GetMapping("/search")
    public ResponseEntity<IPage<TokenDTO>> searchTokenByLastUpdated(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        IPage<TokenDTO> tokenDTOIPage = tokenAppService.findTokenByLastUpdate(start, end, page, size);
        return ResponseEntity.ok(tokenDTOIPage);
    }
}
