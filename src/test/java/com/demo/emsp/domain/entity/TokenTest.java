package com.demo.emsp.domain.entity;

import com.demo.emsp.domain.enums.TokenStatus;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

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

}
