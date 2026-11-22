package com.chavaillaz.client.common.security;

import java.util.function.BiConsumer;

import lombok.Getter;

/**
 * Token authentication, adding {@code Authorization} header with it.
 */
@Getter
public class TokenAuthentication extends AnonymousAuthentication {

    private final String token;

    /**
     * Creates a bearer authentication with the given token.
     *
     * @param token The token to authenticate with
     */
    public TokenAuthentication(String token) {
        this.token = token;
    }

    @Override
    public void fillHeaders(BiConsumer<String, String> addHeader) {
        addHeader.accept("Authorization", "Bearer " + getToken());
    }

}
