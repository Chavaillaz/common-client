package com.chavaillaz.client.common.security;

import java.util.function.BiConsumer;

/**
 * Hold the authentication details to access the desired services.
 * Its implementation needs to fill the query parameters, headers and/or cookies.
 */
public abstract class Authentication {

    /**
     * Fills the credentials in the query parameters using the given method.
     *
     * @param addQueryParameter The filling method
     */
    public abstract void fillQueryParameters(BiConsumer<String, String> addQueryParameter);

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
