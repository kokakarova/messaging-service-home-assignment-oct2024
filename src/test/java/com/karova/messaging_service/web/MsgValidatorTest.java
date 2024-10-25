package com.karova.messaging_service.web;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


class MsgValidatorTest {

    @Test
    void shouldReturn_True_WhenValidUUID() {
        String validUuid = UUID.randomUUID().toString();
        assertTrue(MsgValidator.isValid(validUuid));
    }
    @Test
    void shouldReturn_False_WhenInvalidUUID() {
        String invalidUuid = "6bd3ade7-7daa-4bc7-ba33-3e5879865ax";
        assertFalse(MsgValidator.isValid(invalidUuid));
    }
}