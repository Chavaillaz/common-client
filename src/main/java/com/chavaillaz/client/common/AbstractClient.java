package com.chavaillaz.client.common;

import com.chavaillaz.client.common.security.AnonymousAuthentication;
import com.chavaillaz.client.common.security.Authentication;
import com.chavaillaz.client.common.security.PasswordAuthentication;
import com.chavaillaz.client.common.security.TokenAuthentication;
import com.chavaillaz.client.common.utility.ProxyConfiguration;

/**
 * Abstract class implementing common parts of general client.
 *
 * @param <C> The HTTP client type
 * @param <I> The interface type (to be returned by methods in order to chain calls)
 */
public abstract class AbstractClient<C, I> implements Client<I> {

    protected final String baseUrl;
    protected Authentication authentication;
    protected ProxyConfiguration proxy;

    /**
     * Creates a new abstract client.
     *
     * @param baseUrl The base URL of the endpoints
     */
    protected AbstractClient(String baseUrl) {
        this.baseUrl = baseUrl;
        withAnonymousAuthentication();
    }

    /**
     * Creates a new HTTP client that will be used to communicate with the desired endpoints.
     *
     * @return The HTTP client
     */
    protected abstract C newHttpClient();

    @Override
    public I withProxy(String host, Integer port) {
        this.proxy = ProxyConfiguration.from(host, port);
        return (I) this;
    }

    @Override
    public I withProxy(String url) {
        this.proxy = ProxyConfiguration.from(url);
        return (I) this;
    }

    @Override
    public I withAnonymousAuthentication() {
        this.authentication = new AnonymousAuthentication();
        return (I) this;
    }

    @Override
    public I withTokenAuthentication(String token) {
        this.authentication = new TokenAuthentication(token);
        return (I) this;
    }

    @Override
    public I withUserAuthentication(String username, String password) {
        this.authentication = new PasswordAuthentication(username, password);
        return (I) this;
    }
}
