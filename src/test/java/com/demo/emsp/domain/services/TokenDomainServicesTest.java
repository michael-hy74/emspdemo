package com.demo.emsp.domain.services;

import com.demo.emsp.domain.entity.Token;
import com.demo.emsp.domain.enums.AccountStatus;
import com.demo.emsp.domain.enums.TokenStatus;
import com.demo.emsp.domain.enums.TokenType;
import com.demo.emsp.domain.repository.TokenRepository;
import com.demo.emsp.domain.values.TokenId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TokenDomainServicesTest {

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private TokenDomainService tokenDomainService;

    @Test
    public void testSaveToken_Success() {
        Token token = new Token();
        token.setId("1896180231133728769");
        token.setTokenType(TokenType.RFID);
        token.setTokenStatus(TokenStatus.CREATED);

        Mockito.when(tokenRepository.save(token)).thenReturn(Optional.of(token));
        Token tokenSaved = tokenDomainService.saveToken(token);

        Assertions.assertNotNull(tokenSaved);
        Assertions.assertEquals(token.getId(), tokenSaved.getId());
        Assertions.assertEquals(AccountStatus.CREATED.toString(), tokenSaved.getTokenStatus().toString());
    }

    @Test
    public void testSaveToken_Failure() {
        Token token = new Token();
        token.setId("1896180231133728769");
        token.setTokenType(TokenType.RFID);
        token.setTokenStatus(TokenStatus.CREATED);

        Mockito.when(tokenRepository.save(token)).thenReturn(Optional.<Token>empty());
        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            tokenDomainService.saveToken(token);
        });
        assertEquals("Token saved failed", exception.getMessage());
    }

    @Test
    public void testUpdateTokenStatus_Success() {
        //Token Status -> ACTIVATED
        String tokenId = "1896180232702398465";
        Token tokenExist = new Token();
        tokenExist.setId(tokenId);
        tokenExist.setTokenStatus(TokenStatus.ASSIGNED);
        tokenExist.setLastUpdated(LocalDateTime.now().minusDays(1));

        Token updateRequest = new Token();
        updateRequest.setId(tokenId);
        updateRequest.setTokenStatus(TokenStatus.ACTIVATED);

        Mockito.when(tokenRepository.findById(new TokenId(tokenId))).thenReturn(Optional.of(tokenExist));
        Mockito.when(tokenRepository.update(tokenExist)).thenReturn(Optional.of(tokenExist));

        Token updatedToken = tokenDomainService.updateTokenStatus(updateRequest);

        Assertions.assertNotNull(updatedToken);
        assertEquals(tokenId, updatedToken.getId());
        assertEquals(TokenStatus.ACTIVATED, updatedToken.getTokenStatus());

        Assertions.assertTrue(updatedToken.getLastUpdated().isAfter(LocalDateTime.now().minusMinutes(1)));

        Mockito.verify(tokenRepository, Mockito.times(2)).findById(new TokenId(tokenId));
        Mockito.verify(tokenRepository, Mockito.times(1)).update(tokenExist);


        //Token Status -> DEACTIVATED
        tokenExist.setTokenStatus(TokenStatus.ACTIVATED);
        updateRequest.setTokenStatus(TokenStatus.DEACTIVATED);

        Mockito.when(tokenRepository.findById(new TokenId(tokenId))).thenReturn(Optional.of(tokenExist));
        Mockito.when(tokenRepository.update(tokenExist)).thenReturn(Optional.of(tokenExist));

        updatedToken = tokenDomainService.updateTokenStatus(updateRequest);

        Assertions.assertNotNull(updatedToken);
        assertEquals(tokenId, updatedToken.getId());
        assertEquals(TokenStatus.DEACTIVATED, updatedToken.getTokenStatus());

        Assertions.assertTrue(updatedToken.getLastUpdated().isAfter(LocalDateTime.now().minusMinutes(1)));

        Mockito.verify(tokenRepository, Mockito.times(4)).findById(new TokenId(tokenId));
        Mockito.verify(tokenRepository, Mockito.times(2)).update(tokenExist);
    }

    @Test
    public void testUpdateTokenStatus_Failure() {
        //Get exist token failure
        String tokenId = "1896180232702398465";
        Token updateRequest = new Token();
        updateRequest.setId(tokenId);
        updateRequest.setTokenStatus(TokenStatus.ACTIVATED);

        Mockito.when(tokenRepository.findById(new TokenId(tokenId))).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            tokenDomainService.updateTokenStatus(updateRequest);
        });
        assertEquals("Token Not existed", exception.getMessage());

        //Get exist token success but update token failure
        Token tokenExist = new Token();
        tokenExist.setId(tokenId);
        tokenExist.setTokenStatus(TokenStatus.ASSIGNED);
        tokenExist.setLastUpdated(LocalDateTime.now().minusDays(1));

        Mockito.when(tokenRepository.findById(new TokenId(tokenId))).thenReturn(Optional.of(tokenExist));
        Mockito.when(tokenRepository.update(tokenExist)).thenReturn(Optional.empty());

        exception = Assertions.assertThrows(RuntimeException.class, () -> {
            tokenDomainService.updateTokenStatus(updateRequest);
        });
        assertEquals("Token Update Failed", exception.getMessage());
    }

    @Test
    public void testGetToken_Success() {
        String tokenId = "1896180231133728769";
        Token tokenExist = new Token();
        tokenExist.setId(tokenId);

        Mockito.when(tokenRepository.findById(any(TokenId.class))).thenReturn(Optional.of(tokenExist));

        Token tokenResponse = tokenDomainService.getToken(new TokenId(tokenId));

        Assertions.assertNotNull(tokenResponse);
        Assertions.assertEquals(tokenId, tokenResponse.getId());
    }

    @Test
    public void testGetToken_Failure() {
        TokenId tokenId = new TokenId("1896180231133728769");
        Mockito.when(tokenRepository.findById(tokenId)).thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            tokenDomainService.getToken(tokenId);
        });
        assertEquals("Token not found", exception.getMessage());
    }
}
