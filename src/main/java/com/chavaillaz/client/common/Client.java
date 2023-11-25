package com.chavaillaz.client.common;

/**
 * Base client having methods to set up proxy and authentication.
 *
 * @param <I> The interface type (to be returned by methods in order to chain calls)
 */
public interface Client<I> {

    /**
     * Sets the proxy to use for all requests to the API.
     *
     * @param host The proxy host
     * @param port The proxy port
     * @return The current client instance
     */
    I withProxy(String host, Integer port);

    /**
     * Sets the proxy to use for all requests to the API.
     *
     * @param url The proxy URL
     * @return The current client instance
     */
    I withProxy(String url);

    /**
     * Sets the credentials to use for all requests to the API.
     * Note that the server has to accept a token without username,
     * if it's not the case, please use {@link #withTokenAuthentication(String, String)}.
     *
     * @param token The personal access token
     * @return The current client instance
     */
    I withTokenAuthentication(String token);

    /**
     * Sets the credentials to use for all requests to the API.
     *
     * @param username The username
     * @param token    The personal access token
     * @return The current client instance
     */
    I withTokenAuthentication(String username, String token);

    /**
     * Sets the credentials to use for all requests to the API.
     *
     * @param username The username
     * @param password The password
     * @return The current client instance
     */
    I withUserAuthentication(String username, String password);

    /**
     * Uses the anonymous access if available for all requests to the API.
     *
     * @return The current client instance
     */
    I withAnonymousAuthentication();

}
