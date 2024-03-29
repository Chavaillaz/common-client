package com.chavaillaz.client.common.apache;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.hc.client5.http.protocol.HttpClientContext.COOKIE_STORE;
import static org.apache.hc.core5.http.ContentType.MULTIPART_FORM_DATA;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

import com.chavaillaz.client.common.AbstractHttpClient;
import com.chavaillaz.client.common.security.Authentication;
import com.fasterxml.jackson.databind.JavaType;
import lombok.SneakyThrows;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.async.methods.SimpleRequestBuilder;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.cookie.BasicClientCookie;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.protocol.HttpContext;

/**
 * Abstract class implementing common parts for Apache HTTP.
 */
public abstract class AbstractApacheHttpClient extends AbstractHttpClient implements AutoCloseable {

    protected final CloseableHttpAsyncClient client;

    /**
     * Creates a new abstract client based on Apache HTTP client.
     *
     * @param client         The Apache HTTP client to use
     * @param baseUrl        The base URL of service API
     * @param authentication The authentication information
     */
    protected AbstractApacheHttpClient(CloseableHttpAsyncClient client, String baseUrl, Authentication authentication) {
        super(baseUrl, authentication);
        this.client = client;
        this.client.start();
    }

    /**
     * Enriches a request builder based on the given URL and replaces the parameters in it by the given ones.
     *
     * @param builder    The request builder to use
     * @param url        The URL with possible parameters in it (using braces)
     * @param parameters The parameters value to replace in the URL (in the right order)
     * @return The request builder having the URL and authentication set
     */
    protected SimpleRequestBuilder requestBuilder(SimpleRequestBuilder builder, String url, Object... parameters) {
        builder.setUri(url(url, parameters))
                .setHeader(HEADER_CONTENT_TYPE, HEADER_CONTENT_JSON);
        getAuthentication().fillHeaders(builder::setHeader);
        return builder;
    }

    /**
     * Sends a request and returns a domain object.
     *
     * @param requestBuilder The request builder
     * @param returnType     The domain object type class
     * @param <T>            The domain object type
     * @return A {@link CompletableFuture} with the deserialized domain object
     */
    protected <T> CompletableFuture<T> sendAsync(SimpleRequestBuilder requestBuilder, Class<T> returnType) {
        return sendAsync(requestBuilder, objectMapper.constructType(returnType));
    }

    /**
     * Sends a request and returns a domain object.
     *
     * @param requestBuilder The request builder
     * @param returnType     The domain object type class
     * @param <T>            The domain object type
     * @return A {@link CompletableFuture} with the deserialized domain object
     */
    protected <T> CompletableFuture<T> sendAsync(SimpleRequestBuilder requestBuilder, JavaType returnType) {
        return sendAsyncBase(requestBuilder)
                .thenApply(SimpleHttpResponse::getBodyText)
                .thenApply(body -> deserialize(body, returnType));
    }

    /**
     * Sends a request and returns an input stream.
     *
     * @param requestBuilder The request builder
     * @return A {@link CompletableFuture} with the input stream
     */
    protected CompletableFuture<InputStream> sendAsync(SimpleRequestBuilder requestBuilder) {
        return sendAsyncBase(requestBuilder)
                .thenApply(SimpleHttpResponse::getBodyBytes)
                .thenApply(ByteArrayInputStream::new);
    }

    /**
     * Sends a request and returns the corresponding response.
     *
     * @param requestBuilder The request builder
     * @return A {@link CompletableFuture} with the response
     */
    protected CompletableFuture<SimpleHttpResponse> sendAsyncBase(SimpleRequestBuilder requestBuilder) {
        SimpleHttpRequest request = requestBuilder.build();
        CompletableFuture<SimpleHttpResponse> completableFuture = new CompletableFuture<>();
        client.execute(request, createContext(), new CompletableFutureCallback(this, request, completableFuture));
        return completableFuture;
    }

    /**
     * Sends a multipart request and returns a domain object.
     *
     * @param requestBuilder   The request builder
     * @param multipartBuilder The multipart builder
     * @param returnType       The domain object type class
     * @param <T>              The domain object type
     * @return A {@link CompletableFuture} with the deserialized domain object
     */
    @SneakyThrows
    protected <T> CompletableFuture<T> sendAsync(SimpleRequestBuilder requestBuilder, MultipartEntityBuilder multipartBuilder, Class<T> returnType) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String boundary = randomAlphanumeric(16);
        multipartBuilder.setBoundary(boundary);
        try (HttpEntity entity = multipartBuilder.build()) {
            entity.writeTo(outputStream);
        }
        requestBuilder.setHeader(HEADER_CONTENT_TYPE, MULTIPART_FORM_DATA.getMimeType() + "; boundary=" + boundary)
                .setBody(outputStream.toByteArray(), MULTIPART_FORM_DATA);
        return sendAsync(requestBuilder, returnType);
    }

    /**
     * Creates a local context for the query to be launched.
     *
     * @return The corresponding context
     */
    protected HttpContext createContext() {
        HttpContext localContext = HttpClientContext.create();
        BasicCookieStore cookieStore = new BasicCookieStore();
        getAuthentication().fillCookies((key, value) -> addCookie(cookieStore, key, value));
        localContext.setAttribute(COOKIE_STORE, cookieStore);
        return localContext;
    }

    /**
     * Adds a cookie to the given store.
     *
     * @param store The cookie store
     * @param key   The cookie key
     * @param value The cookie value
     */
    protected void addCookie(CookieStore store, String key, String value) {
        BasicClientCookie cookie = new BasicClientCookie(key, value);
        cookie.setDomain(getBaseUrl());
        cookie.setPath("/");
        store.addCookie(cookie);
    }

    @Override
    public void close() throws Exception {
        client.close();
    }

}
