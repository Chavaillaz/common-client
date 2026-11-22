package com.chavaillaz.client.common.security;

import static com.chavaillaz.client.common.utility.Utils.encodeBase64;

import java.util.function.BiConsumer;

import lombok.Getter;

/**
 * Basic authentication with username and password, adding {@code Authorization} header with them.
 */
@Getter
public class PasswordAuthentication extends AnonymousAuthentication {

    private final String username;
    private final String password;

    /**
     * Creates a basic authentication with the given credentials.
     *
     * @param username The name of the user
     * @param password The password of the user
     */
    public PasswordAuthentication(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public void fillHeaders(BiConsumer<String, String> addHeader) {
        addHeader.accept("Authorization", "Basic " + encodeBase64(getUsername() + ":" + getPassword()));
    }

}
