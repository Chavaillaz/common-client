package com.chavaillaz.client.common.security;

import java.util.function.BiConsumer;

/**
 * Anonymous authentication which is not adding anything to headers and/or cookies.
 */
public class AnonymousAuthentication extends Authentication {

    /**
     * Creates an authentication adding nothing to the requests.
     */
    public AnonymousAuthentication() {
        // Nothing to set up, an anonymous request carrying no credentials
    }

    @Override
    public void fillHeaders(BiConsumer<String, String> addHeader) {
        // Nothing to do
    }

    @Override
    public void fillCookies(BiConsumer<String, String> addCookie) {
        // Nothing to do
    }

}
