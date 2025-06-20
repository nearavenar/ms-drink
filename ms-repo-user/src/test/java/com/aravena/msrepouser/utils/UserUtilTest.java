package com.aravena.msrepouser.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserUtilTest {
    @Test
    void maskPasswordDefault() {
        String expected = "****";
        String result = UserUtil.maskPassword("pass", 4);
        assertNotNull(result);
        assertEquals(expected, result);
    }
    @Test
    void maskPassword() {
        String expected = "pa****rd";
        String result = UserUtil.maskPassword("password", 4);
        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    void maskPasswordNull() {
        String expected = "";
        String result = UserUtil.maskPassword(null, 4);
        assertNotNull(result);
        assertEquals(expected, result);
    }
}