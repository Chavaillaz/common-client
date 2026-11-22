package com.chavaillaz.client.common.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("TokenAuthentication")
class TokenAuthenticationTest {

    @Test
    @DisplayName("Fills a bearer authorization header, and no cookie")
    void testTokenAuthentication() {
        TokenAuthentication authentication = new TokenAuthentication("Token");
        assertThat(authentication.getToken()).isEqualTo("Token");

        Map<String, String> headers = new HashMap<>();
        authentication.fillHeaders(headers::put);
        assertThat(headers).containsOnly(entry("Authorization", "Bearer Token"));

        Map<String, String> cookies = new HashMap<>();
        authentication.fillCookies(cookies::put);
        assertThat(cookies).isEmpty();
    }

}
