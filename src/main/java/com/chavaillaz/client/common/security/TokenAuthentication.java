package com.chavaillaz.client.common.security;

import static com.chavaillaz.client.common.utility.Utils.encodeBase64;

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
        addHeader.accept("Authorization", "Bearer " + encodeBase64(getToken()));
    }

}
