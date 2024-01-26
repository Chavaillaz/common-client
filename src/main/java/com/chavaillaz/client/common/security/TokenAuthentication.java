package com.chavaillaz.client.common.security;

import java.util.function.BiConsumer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Token authentication, adding {@code Authorization} header with it.
 */
@Getter
@RequiredArgsConstructor
public class TokenAuthentication extends AnonymousAuthentication {

    private final String token;

    @Override
    public void fillHeaders(BiConsumer<String, String> addHeader) {
        addHeader.accept("Authorization", "Bearer " + getToken());
    }

}
