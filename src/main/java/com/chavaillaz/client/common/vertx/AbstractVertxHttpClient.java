package com.chavaillaz.client.common.vertx;

import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

import com.chavaillaz.client.common.AbstractHttpClient;
import com.chavaillaz.client.common.Authentication;
import com.fasterxml.jackson.databind.JavaType;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import lombok.extern.slf4j.Slf4j;

/**
 * Abstract class implementing common parts for Vert.x HTTP.
 *
 * @param <A> The authentication type
 */
@Slf4j
public class AbstractVertxHttpClient<A extends Authentication> extends AbstractHttpClient<A> implements AutoCloseable {

    protected WebClient client;

    /**
     * Creates a new abstract client based on Vert.x HTTP client.
     *
     * @param vertx          The Vert.x instance to create the web client
     * @param options        The web client options
     * @param baseUrl        The base URL of endpoints
     * @param authentication The authentication information
     */
    protected AbstractVertxHttpClient(Vertx vertx, WebClientOptions options, String baseUrl, A authentication) {
        super(baseUrl, authentication);
        this.client = WebClient.create(vertx, options);
    }

    /**
     * Creates a new abstract client based on Vert.x HTTP client.
     *
     * @param httpClient     The HTTP client to wrap in the Vert.x web client
     * @param baseUrl        The base URL of endpoints
     * @param authentication The authentication information
     */
    protected AbstractVertxHttpClient(HttpClient httpClient, String baseUrl, A authentication) {
        super(baseUrl, authentication);
        this.client = WebClient.wrap(httpClient);
    }

    /**
     * Creates a request based on the given URL and replaces the parameters in it by the given ones.
     *
     * @param method     The HTTP method for the HTTP request to build
     * @param url        The URL with possible parameters in it (using braces)
     * @param parameters The parameters value to replace in the URL (in the right order)
     * @return The request having the URL and authorization header set
     */
    protected HttpRequest<Buffer> requestBuilder(HttpMethod method, String url, Object... parameters) {
        return client.request(method, url(url, parameters).toString())
                .putHeader(HEADER_AUTHORIZATION, getAuthentication().getAuthorizationHeader())
                .putHeader(HEADER_CONTENT_TYPE, HEADER_CONTENT_JSON);
    }

    /**
     * Sends a request and returns a domain object.
     *
     * @param request    The request
     * @param returnType The domain object type class
     * @param <T>        The domain object type
     * @return A {@link CompletableFuture} with the deserialized domain object
     */
    protected <T> CompletableFuture<T> sendAsync(HttpRequest<Buffer> request, Class<T> returnType) {
        return sendAsync(request, objectMapper.constructType(returnType));
    }

    /**
     * Sends a request and returns a domain object.
     *
     * @param request    The request
     * @param returnType The domain object type class
     * @param <T>        The domain object type
     * @return A {@link CompletableFuture} with the deserialized domain object
     */
    protected <T> CompletableFuture<T> sendAsync(HttpRequest<Buffer> request, JavaType returnType) {
        CompletableFuture<HttpResponse<Buffer>> completableFuture = new CompletableFuture<>();
        request.send()
                .onSuccess(response -> handleResponse(response, completableFuture))
                .onFailure(completableFuture::completeExceptionally);
        return completableFuture.thenApply(HttpResponse::bodyAsString)
                .thenApply(body -> deserialize(body, returnType));
    }

    /**
     * Sends a request and returns an input stream.
     *
     * @param request The request
     * @return A {@link CompletableFuture} with the input stream
     */
    protected CompletableFuture<InputStream> sendAsync(HttpRequest<Buffer> request) {
        CompletableFuture<HttpResponse<Buffer>> completableFuture = new CompletableFuture<>();
        request.send()
                .onSuccess(response -> handleResponse(response, completableFuture))
                .onFailure(completableFuture::completeExceptionally);
        return completableFuture.thenApply(HttpResponse::body)
                .thenApply(VertxInputStream::new);
    }

    protected void handleResponse(HttpResponse<Buffer> response, CompletableFuture<HttpResponse<Buffer>> completableFuture) {
        if (response.statusCode() >= 400) {
            completableFuture.completeExceptionally(responseException(response.statusCode(), response.bodyAsString()));
        } else {
            completableFuture.complete(response);
        }
    }

    @Override
    public void close() throws Exception {
        client.close();
    }

}
