package com.chavaillaz.client.common.security;

import java.util.function.BiConsumer;

/**
 * Hold the authentication details to access the desired services.
 * Its implementation needs to fill headers and/or cookies.
 */
public abstract class Authentication {

    /**
     * Creates an authentication, which a subclass fills the headers and cookies of.
     */
    protected Authentication() {
        // Nothing to set up, the subclass carrying what the requests need
    }

    /**
     * Fills the credentials in the headers using the given method.
     *
     * @param addHeader The filling method
     */
    public abstract void fillHeaders(BiConsumer<String, String> addHeader);

    /**
     * Fills the credentials in the cookies using the given method.
     *
     * @param addCookie The filling method
     */
    public abstract void fillCookies(BiConsumer<String, String> addCookie);


}
