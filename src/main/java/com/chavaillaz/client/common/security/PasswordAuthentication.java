package com.chavaillaz.client.common.security;

import static com.chavaillaz.client.common.utility.Utils.encodeBase64;

import java.util.function.BiConsumer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Basic authentication with username and password, adding {@code Authorization} header with them.
 */
@Getter
@RequiredArgsConstructor
public class PasswordAuthentication extends AnonymousAuthentication {

    private final String username;
    private final String password;

    @Override
    public void fillHeaders(BiConsumer<String, String> addHeader) {
        addHeader.accept("Authorization", "Basic " + encodeBase64(getUsername() + ":" + getPassword()));
    }

}
