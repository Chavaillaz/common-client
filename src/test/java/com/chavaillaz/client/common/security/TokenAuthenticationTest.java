package com.chavaillaz.client.common.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class TokenAuthenticationTest {

    @Test
    void testTokenAuthentication() {
        TokenAuthentication authentication = new TokenAuthentication("Token");
        assertEquals("Token", authentication.getToken());

        Map<String, String> headers = new HashMap<>();
        authentication.fillHeaders(headers::put);
        assertEquals(1, headers.size());

        String authorization = headers.get("Authorization");
        assertNotNull(authorization);
        assertEquals("Bearer Token", authorization);

        Map<String, String> cookies = new HashMap<>();
        authentication.fillCookies(cookies::put);
        assertTrue(cookies.isEmpty());
    }

}
