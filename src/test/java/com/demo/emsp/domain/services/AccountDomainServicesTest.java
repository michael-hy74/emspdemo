package com.demo.emsp.domain.services;

import com.demo.emsp.domain.entity.Account;
import com.demo.emsp.domain.entity.Token;
import com.demo.emsp.domain.enums.AccountStatus;
import com.demo.emsp.domain.enums.TokenStatus;
import com.demo.emsp.domain.enums.TokenType;
import com.demo.emsp.domain.repository.AccountRepository;
import com.demo.emsp.domain.repository.TokenRepository;
import com.demo.emsp.domain.values.AccountId;
import com.demo.emsp.domain.values.FleetSolution;
import com.demo.emsp.domain.values.ServiceId;
import com.demo.emsp.domain.values.TokenId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class AccountDomainServicesTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private AccountDomainService accountDomainService;

    @InjectMocks
    private TokenDomainService tokenDomainService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveAccount_Success() {
        Account account = new Account();
        account.setId("1896180231133728769");
        account.setServiceId(new ServiceId("services-1"));
        account.setFleetSolution(new FleetSolution("solution-1"));
        account.setAccountStatus(AccountStatus.CREATED);

        Mockito.when(accountRepository.save(account)).thenReturn(Optional.of(account));
        Account savedAccount = accountDomainService.saveAccount(account);

        Assertions.assertNotNull(savedAccount);
        Assertions.assertEquals(account.getId(), savedAccount.getId());
        Assertions.assertEquals(AccountStatus.CREATED, savedAccount.getAccountStatus());
    }

    @Test
    public void testSaveAccount_Failure() {
        Account account = new Account();
        Mockito.when(accountRepository.save(account)).thenReturn(Optional.<Account>empty());
        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            accountDomainService.saveAccount(account);
        });
        assertEquals("Account saved failed", exception.getMessage());
    }

    @Test
    public void testUpdateAccountStatus_Success() {
        //Account Status -> ACTIVATED
        String accountId = "1896180231133728769";
        Account accountExist = new Account();
        accountExist.setId(accountId);
        accountExist.setAccountStatus(AccountStatus.CREATED);
        accountExist.setLastUpdated(LocalDateTime.now().minusDays(1));

        Account updateRequest = new Account();
        updateRequest.setId(accountId);
        updateRequest.setAccountStatus(AccountStatus.ACTIVATED);

        Mockito.when(accountRepository.findById(new AccountId(accountId))).thenReturn(Optional.of(accountExist));
        Mockito.when(accountRepository.update(accountExist)).thenReturn(Optional.of(accountExist));

        Account updatedAccount = accountDomainService.updateAccountStatus(updateRequest);

        Assertions.assertNotNull(updatedAccount);
        assertEquals(accountId, updatedAccount.getId());
        assertEquals(AccountStatus.ACTIVATED, updatedAccount.getAccountStatus());

        Assertions.assertTrue(updatedAccount.getLastUpdated().isAfter(LocalDateTime.now().minusMinutes(1)));

        Mockito.verify(accountRepository, Mockito.times(2)).findById(new AccountId(accountId));
        Mockito.verify(accountRepository, Mockito.times(1)).update(accountExist);


        //Account Status -> DEACTIVATED
        accountExist.setAccountStatus(AccountStatus.ACTIVATED);
        updateRequest.setAccountStatus(AccountStatus.DEACTIVATED);

        Mockito.when(accountRepository.findById(new AccountId(accountId))).thenReturn(Optional.of(accountExist));
        Mockito.when(accountRepository.update(accountExist)).thenReturn(Optional.of(accountExist));

        updatedAccount = accountDomainService.updateAccountStatus(updateRequest);

        Assertions.assertNotNull(updatedAccount);
        assertEquals(accountId, updatedAccount.getId());
        assertEquals(AccountStatus.DEACTIVATED, updatedAccount.getAccountStatus());

        Assertions.assertTrue(updatedAccount.getLastUpdated().isAfter(LocalDateTime.now().minusMinutes(1)));

        Mockito.verify(accountRepository, Mockito.times(4)).findById(new AccountId(accountId));
        Mockito.verify(accountRepository, Mockito.times(2)).update(accountExist);
    }

    @Test
    public void testUpdateAccountStatus_Failure() {
        //Get exist account failure
        String accountId = "1896180231133728769";
        Account updateRequest = new Account();
        updateRequest.setId(accountId);
        updateRequest.setAccountStatus(AccountStatus.ACTIVATED);

        Mockito.when(accountRepository.findById(new AccountId(accountId))).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            accountDomainService.updateAccountStatus(updateRequest);
        });
        assertEquals("Account Not existed", exception.getMessage());

        //Get exist account success but update account failure
        Account accountExist = new Account();
        accountExist.setId(accountId);
        accountExist.setAccountStatus(AccountStatus.CREATED);
        accountExist.setLastUpdated(LocalDateTime.now().minusDays(1));

        Mockito.when(accountRepository.findById(new AccountId(accountId))).thenReturn(Optional.of(accountExist));
        Mockito.when(accountRepository.update(accountExist)).thenReturn(Optional.empty());

        exception = Assertions.assertThrows(RuntimeException.class, () -> {
            accountDomainService.updateAccountStatus(updateRequest);
        });
        assertEquals("Account Update Failed", exception.getMessage());
    }

    @Test
    public void testAssignToken_Success() {
        String accountId = "1896180231133728769";
        Account accountExist = new Account();
        accountExist.setId(accountId);
        accountExist.setAccountStatus(AccountStatus.ACTIVATED);

        String tokenId = "1896180232702398465";
        Token tokenExist = new Token();
        tokenExist.setId(tokenId);
        tokenExist.setAccountId(new AccountId(accountId));
        tokenExist.setTokenType(TokenType.RFID);
        tokenExist.setTokenStatus(TokenStatus.ASSIGNED);

        List<Token> tokenList = new ArrayList<>();
        tokenList.add(tokenExist);
        accountExist.setTokens(tokenList);

        Token tokenRequest = new Token();
        tokenRequest.setId(tokenId);
        tokenRequest.setAccountId(new AccountId(accountId));
        tokenRequest.setTokenType(TokenType.RFID);
        tokenRequest.setTokenStatus(TokenStatus.CREATED);

        Mockito.when(tokenRepository.findById(new TokenId(tokenId))).thenReturn(Optional.of(tokenExist));
        Mockito.when(accountRepository.findById(new AccountId(accountId))).thenReturn(Optional.of(accountExist));
        Mockito.when(tokenRepository.update(any(Token.class))).thenReturn(Optional.of(tokenExist));

        Account updatedAccount = accountDomainService.assignToken(tokenRequest);

        Assertions.assertNotNull(updatedAccount);
        assertEquals(accountId, updatedAccount.getId());
        assertEquals(TokenStatus.ASSIGNED, updatedAccount.getTokens().get(0).getTokenStatus());

        Mockito.verify(tokenRepository, Mockito.times(2)).findById(new TokenId(tokenId));
        Mockito.verify(accountRepository, Mockito.times(1)).findById(new AccountId(accountId));
    }

    @Test
    public void testAssignToken_Failure() {
        String accountId = "1896180231133728769";
        Account accountExist = new Account();
        accountExist.setId(accountId);
        accountExist.setAccountStatus(AccountStatus.CREATED);

        String tokenId = "1896180232702398465";
        Token tokenExist = new Token();
        tokenExist.setId(tokenId);
        tokenExist.setAccountId(new AccountId(accountId));
        tokenExist.setTokenType(TokenType.RFID);
        tokenExist.setTokenStatus(TokenStatus.ASSIGNED);

        List<Token> tokenList = new ArrayList<>();
        tokenList.add(tokenExist);
        accountExist.setTokens(tokenList);

        Token tokenRequest = new Token();
        tokenRequest.setId(tokenId);
        tokenRequest.setAccountId(new AccountId(accountId));
        tokenRequest.setTokenType(TokenType.RFID);
        tokenRequest.setTokenStatus(TokenStatus.CREATED);

        Mockito.when(tokenRepository.findById(new TokenId(tokenId))).thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            accountDomainService.assignToken(tokenRequest);
        });
        assertEquals("Token Not existed", exception.getMessage());


        Mockito.when(tokenRepository.findById(new TokenId(tokenId))).thenReturn(Optional.of(tokenExist));
        Mockito.when(accountRepository.findById(new AccountId(accountId))).thenReturn(Optional.empty());
        exception = Assertions.assertThrows(RuntimeException.class, () -> {
            accountDomainService.assignToken(tokenRequest);
        });
        assertEquals("Account Not existed", exception.getMessage());


        Mockito.when(tokenRepository.findById(new TokenId(tokenId))).thenReturn(Optional.of(tokenExist));
        Mockito.when(accountRepository.findById(new AccountId(accountId))).thenReturn(Optional.of(accountExist));
        exception = Assertions.assertThrows(IllegalStateException.class, () -> {
            accountDomainService.assignToken(tokenRequest);
        });
        assertEquals("Token must be assign to ACTIVATED Account", exception.getMessage());

        accountExist.setAccountStatus(AccountStatus.ACTIVATED);
        Mockito.when(tokenRepository.findById(new TokenId(tokenId))).thenReturn(Optional.of(tokenExist));
        Mockito.when(accountRepository.findById(new AccountId(accountId))).thenReturn(Optional.of(accountExist));
        Mockito.when(tokenRepository.update(any(Token.class))).thenReturn(Optional.empty());
        exception = Assertions.assertThrows(RuntimeException.class, () -> {
            accountDomainService.assignToken(tokenRequest);
        });
        assertEquals("Token Update Failed", exception.getMessage());
    }

    @Test
    public void testGetAccount_Success() {
        String accountId = "1896180231133728769";
        Account accountExist = new Account();
        accountExist.setId(accountId);

        Mockito.when(accountRepository.findById(any(AccountId.class))).thenReturn(Optional.of(accountExist));

        Account accountResponse = accountDomainService.getAccount(new AccountId(accountId));

        Assertions.assertNotNull(accountResponse);
        Assertions.assertEquals(accountId, accountResponse.getId());
    }

    @Test
    public void testGetAccount_Failure() {
        AccountId accountId = new AccountId("1896180231133728769");
        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            accountDomainService.getAccount(accountId);
        });
        assertEquals("Account Not existed", exception.getMessage());
    }
}
