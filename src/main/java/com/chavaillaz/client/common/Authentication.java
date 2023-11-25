package com.chavaillaz.client.common;

import static com.chavaillaz.client.common.Authentication.AuthenticationType.ANONYMOUS;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Authentication details to access the desired services.
 */
@Data
@RequiredArgsConstructor
public abstract class Authentication {

    private final AuthenticationType type;
    private String username;
    private String password;

    /**
     * Creates a new authentication configuration using anonymous access.
     */
    public Authentication() {
        this(ANONYMOUS);
    }

    /**
     * Creates a new authentication configuration.
     *
     * @param type     The type of authentication
     * @param username The username or {@code null} for anonymous access
     * @param password The password, token or {@code null} for anonymous access
     */
    protected Authentication(AuthenticationType type, String username, String password) {
        this(type);
        this.username = username;
        this.password = password;
    }

    /**
     * Gets the HTTP Header {@code Authorization} to transmit for authentication.
     *
     * @return The authorization header
     */
    public abstract String getAuthorizationHeader();

    /**
     * Types of authentication managed by the client.
     */
    public enum AuthenticationType {
        ANONYMOUS,
        PASSWORD,
        TOKEN
    }

}
