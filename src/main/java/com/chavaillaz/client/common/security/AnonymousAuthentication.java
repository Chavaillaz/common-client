package com.chavaillaz.client.common.security;

import java.util.function.BiConsumer;

/**
 * Anonymous authentication which is not adding anything to query parameters, headers or cookies.
 */
public class AnonymousAuthentication extends Authentication {

    @Override
    public void fillHeaders(BiConsumer<String, String> addHeader) {
        // Nothing to do
    }

    @Override
    public void fillCookies(BiConsumer<String, String> addCookie) {
        // Nothing to do
    }

}
