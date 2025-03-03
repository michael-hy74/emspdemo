package com.demo.emsp.domain.entity;

import com.demo.emsp.domain.enums.TokenStatus;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TokenTest {
    @Test
    public void testTokenActivateSuccess() {
        Token token = new Token();
        token.setTokenStatus(TokenStatus.ASSIGNED);
        LocalDateTime beforeLastUpdated = token.getLastUpdated();

        token.activate();
        assertEquals(TokenStatus.ACTIVATED, token.getTokenStatus());
        assertNotEquals(beforeLastUpdated, token.getTokenStatus());
    }

    @Test
    public void testTokenActiveFailure() {
        Token token = new Token();
        token.setTokenStatus(TokenStatus.CREATED);
        LocalDateTime originalLastUpdated = token.getLastUpdated();

        assertThrows(IllegalStateException.class, () -> token.activate());
        assertEquals(TokenStatus.CREATED, token.getTokenStatus());
        assertEquals(originalLastUpdated, token.getLastUpdated());
    }

    @Test
    public void testTokenDeActivateSuccess() {
        Token token = new Token();
        token.setTokenStatus(TokenStatus.ACTIVATED);
        LocalDateTime beforeLastUpdated = token.getLastUpdated();

        token.deactivate();
        assertEquals(TokenStatus.DEACTIVATED, token.getTokenStatus());
        assertNotEquals(beforeLastUpdated, token.getTokenStatus());
    }

    @Test
    public void testTokenDeActiveFailure() {
        Token token = new Token();
        token.setTokenStatus(TokenStatus.DEACTIVATED);
        LocalDateTime originalLastUpdated = token.getLastUpdated();

        assertThrows(IllegalStateException.class, () -> token.deactivate());
        assertEquals(TokenStatus.DEACTIVATED, token.getTokenStatus());
        assertEquals(originalLastUpdated, token.getLastUpdated());
    }

    @Test
    public void testTokenAssignSuccess() {
        Token token = new Token();
        token.setTokenStatus(TokenStatus.CREATED);
        LocalDateTime beforeLastUpdated = token.getLastUpdated();

        String assignTo = "1896190525277011970";
        token.assignTo(assignTo);
        assertEquals(TokenStatus.ASSIGNED, token.getTokenStatus());
        assertEquals("1896190525277011970", token.getAccountId().getValue());
        assertNotEquals(beforeLastUpdated, token.getTokenStatus());
    }

    @Test
    public void testTokenAssignFailure() {
        Token token = new Token();
        token.setTokenStatus(TokenStatus.ACTIVATED);
        LocalDateTime originalLastUpdated = token.getLastUpdated();

        assertThrows(IllegalStateException.class, () -> token.assignTo("1896190525277011970"));
        assertEquals(TokenStatus.ACTIVATED, token.getTokenStatus());
        Optional.ofNullable(token.getAccountId())
                .ifPresent(accountId -> {
                    assertNotEquals("1896190525277011970", accountId.getValue());
                });
        assertEquals(originalLastUpdated, token.getLastUpdated());
    }
}
