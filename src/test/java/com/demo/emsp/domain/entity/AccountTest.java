package com.demo.emsp.domain.entity;

import com.demo.emsp.domain.enums.AccountStatus;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AccountTest {

    @Test
    public void testAccountActivateSuccess() {
        Account account = new Account();
        account.setAccountStatus(AccountStatus.CREATED);
        LocalDateTime beforeLastUpdated = account.getLastUpdated();

        account.activate();
        assertEquals(AccountStatus.ACTIVATED, account.getAccountStatus());
        assertNotEquals(beforeLastUpdated, account.getLastUpdated());
    }

    @Test
    public void testAccountActiveFailure() {
        Account account = new Account();
        account.setAccountStatus(AccountStatus.DEACTIVATED);
        LocalDateTime originalLastUpdated = account.getLastUpdated();

        assertThrows(IllegalStateException.class, () -> account.activate());
        assertEquals(AccountStatus.DEACTIVATED, account.getAccountStatus());
        assertEquals(originalLastUpdated, account.getLastUpdated());
    }

    @Test
    public void testAccountDeActivateSuccess() {
        Account account = new Account();
        account.setAccountStatus(AccountStatus.ACTIVATED);
        LocalDateTime beforeLastUpdated = account.getLastUpdated();

        account.deactivate();
        assertEquals(AccountStatus.DEACTIVATED, account.getAccountStatus());
        assertNotEquals(beforeLastUpdated, account.getLastUpdated());
    }

    @Test
    public void testAccountDeActiveFailure() {
        Account account = new Account();
        account.setAccountStatus(AccountStatus.CREATED);
        LocalDateTime originalLastUpdated = account.getLastUpdated();

        assertThrows(IllegalStateException.class, () -> account.deactivate());
        assertEquals(AccountStatus.CREATED, account.getAccountStatus());
        assertEquals(originalLastUpdated, account.getLastUpdated());
    }
}
