package com.chavaillaz.client.common.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("PasswordAuthentication")
class PasswordAuthenticationTest {

    @Test
    @DisplayName("Fills a basic authorization header, and no cookie")
    void testPasswordAuthentication() {
        PasswordAuthentication authentication = new PasswordAuthentication("User", "Password");
        assertThat(authentication.getUsername()).isEqualTo("User");
        assertThat(authentication.getPassword()).isEqualTo("Password");

        Map<String, String> headers = new HashMap<>();
        authentication.fillHeaders(headers::put);
        assertThat(headers).containsOnly(entry("Authorization", "Basic VXNlcjpQYXNzd29yZA=="));

        Map<String, String> cookies = new HashMap<>();
        authentication.fillCookies(cookies::put);
        assertThat(cookies).isEmpty();
    }

}
