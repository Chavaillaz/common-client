package com.chavaillaz.client.common.vertx;

import static com.chavaillaz.client.common.utility.Utils.getCookieHeader;

import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

import com.chavaillaz.client.common.AbstractHttpClient;
import com.chavaillaz.client.common.security.Authentication;
import com.fasterxml.jackson.databind.JavaType;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;

/**
 * Abstract class implementing common parts for Vert.x HTTP.
 */
public class AbstractVertxHttpClient extends AbstractHttpClient implements AutoCloseable {

    protected WebClient client;

    /**
     * Creates a new abstract client based on Vert.x HTTP client.
     *
     * @param client         The web client to use
     * @param baseUrl        The base URL of endpoints
     * @param authentication The authentication information
     */
    protected AbstractVertxHttpClient(WebClient client, String baseUrl, Authentication authentication) {
        super(baseUrl, authentication);
        this.client = client;
    }

    /**
     * Creates a request based on the given URL and replaces the parameters in it by the given ones.
     *
     * @param method     The HTTP method for the request to build
     * @param url        The URL with possible parameters in it (using braces)
     * @param parameters The parameters value to replace in the URL (in the right order)
     * @return The request having the URL and authentication set
     */
    protected HttpRequest<Buffer> requestBuilder(HttpMethod method, String url, Object... parameters) {
        var request = client.requestAbs(method, url(url, parameters).toString())
                .putHeader(HEADER_CONTENT_TYPE, HEADER_CONTENT_JSON);
        getAuthentication().fillHeaders(request::putHeader);
        getCookieHeader(getAuthentication()).ifPresent(value -> request.putHeader(HEADER_COOKIE, value));
        return request;
    }

    /**
     * Creates a body buffer containing the given object serialized as JSON.
     *
     * @param object The object to serialize
     * @return The corresponding buffer for the request
     */
    protected Buffer body(Object object) {
        return Buffer.buffer(serialize(object));
    }

    /**
     * Handles the request sent and returns a domain object.
     *
     * @param future     The future response
     * @param returnType The domain object type class
     * @param <T>        The domain object type
     * @return A {@link CompletableFuture} with the deserialized domain object
     */
    protected <T> CompletableFuture<T> handleAsync(Future<HttpResponse<Buffer>> future, Class<T> returnType) {
        return handleAsync(future, objectMapper.constructType(returnType));
    }

    /**
     * Handles the request sent and returns a domain object.
     *
     * @param future     The future response
     * @param returnType The domain object type class
     * @param <T>        The domain object type
     * @return A {@link CompletableFuture} with the deserialized domain object
     */
    protected <T> CompletableFuture<T> handleAsync(Future<HttpResponse<Buffer>> future, JavaType returnType) {
        return handleAsyncBase(future)
                .thenApply(HttpResponse::bodyAsString)
                .thenApply(body -> deserialize(body, returnType));
    }

    /**
     * Handles the request sent and returns an input stream.
     *
     * @param future The future response
     * @return A {@link CompletableFuture} with the input stream
     */
    protected CompletableFuture<InputStream> handleAsync(Future<HttpResponse<Buffer>> future) {
        return handleAsyncBase(future)
                .thenApply(HttpResponse::body)
                .thenApply(VertxInputStream::new);
    }

    /**
     * Handles the request sent and returns the corresponding response buffer.
     *
     * @param future The future response
     * @return A {@link CompletableFuture} with the response buffer
     */
    protected CompletableFuture<HttpResponse<Buffer>> handleAsyncBase(Future<HttpResponse<Buffer>> future) {
        CompletableFuture<HttpResponse<Buffer>> completableFuture = new CompletableFuture<>();
        future.onSuccess(response -> handleResponse(response, completableFuture))
                .onFailure(completableFuture::completeExceptionally);
        return completableFuture;
    }

    /**
     * Handles the response by transmitting its state to the given {@link CompletableFuture}.
     *
     * @param response          The HTTP response
     * @param completableFuture The completable future to update
     */
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
