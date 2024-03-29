package com.chavaillaz.client.common.okhttp;

import static com.chavaillaz.client.common.utility.Utils.getCookieHeader;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

import com.chavaillaz.client.common.AbstractHttpClient;
import com.chavaillaz.client.common.security.Authentication;
import com.fasterxml.jackson.databind.JavaType;
import lombok.SneakyThrows;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Abstract class implementing common parts for OkHttp.
 */
public abstract class AbstractOkHttpClient extends AbstractHttpClient implements AutoCloseable {

    public static final RequestBody EMPTY_BODY = RequestBody.create(EMPTY, null);
    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse(HEADER_CONTENT_JSON);
    public static final MediaType MEDIA_TYPE_XML = MediaType.parse(HEADER_CONTENT_XML);

    protected final OkHttpClient client;

    /**
     * Creates a new abstract client based on OkHttp client.
     *
     * @param client         The OkHttp client to use
     * @param baseUrl        The base URL of service API
     * @param authentication The authentication information
     */
    protected AbstractOkHttpClient(OkHttpClient client, String baseUrl, Authentication authentication) {
        super(baseUrl, authentication);
        this.client = client;
    }

    /**
     * Creates a request builder based on the given URL and replaces the parameters in it by the given ones.
     *
     * @param url        The URL with possible parameters in it (using braces)
     * @param parameters The parameters value to replace in the URL (in the right order)
     * @return The request builder having the URL and authentication set
     */
    protected Request.Builder requestBuilder(String url, Object... parameters) {
        var requestBuilder = new Request.Builder()
                .url(url(url, parameters).toString())
                .header(HEADER_CONTENT_TYPE, HEADER_CONTENT_JSON);
        getAuthentication().fillHeaders(requestBuilder::header);
        getCookieHeader(getAuthentication()).ifPresent(value -> requestBuilder.header(HEADER_COOKIE, value));
        return requestBuilder;
    }

    /**
     * Creates a request body with the given object serialized as JSON.
     *
     * @param object The object to serialize
     * @return The corresponding request body
     */
    protected RequestBody body(Object object) {
        return RequestBody.create(serialize(object), MEDIA_TYPE_JSON);
    }

    /**
     * Creates an empty request body.
     *
     * @return The corresponding request body
     */
    protected RequestBody body() {
        return EMPTY_BODY;
    }

    /**
     * Sends a request and returns a domain object.
     *
     * @param requestBuilder The request builder
     * @param returnType     The domain object type class
     * @param <T>            The domain object type
     * @return A {@link CompletableFuture} with the deserialized domain object
     */
    protected <T> CompletableFuture<T> sendAsync(Request.Builder requestBuilder, Class<T> returnType) {
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
    protected <T> CompletableFuture<T> sendAsync(Request.Builder requestBuilder, JavaType returnType) {
        return sendAsyncBase(requestBuilder)
                .thenApply(response -> handleResponse(response, returnType));
    }

    /**
     * Sends a request and returns an input stream.
     *
     * @param requestBuilder The request builder
     * @return A {@link CompletableFuture} with the input stream
     */
    protected CompletableFuture<InputStream> sendAsync(Request.Builder requestBuilder) {
        return sendAsyncBase(requestBuilder)
                .thenApply(Response::body)
                .thenApply(ResponseBody::byteStream);
    }

    /**
     * Sends a request and returns the corresponding response.
     *
     * @param requestBuilder The request builder
     * @return A {@link CompletableFuture} with the response
     */
    protected CompletableFuture<Response> sendAsyncBase(Request.Builder requestBuilder) {
        CompletableFuture<Response> completableFuture = new CompletableFuture<>();
        client.newCall(requestBuilder.build()).enqueue(new CompletableFutureCallback(this, completableFuture));
        return completableFuture;
    }

    /**
     * Handles the response by converting it to the given type.
     *
     * @param response The HTTP response
     * @param type     The type in which convert the response body
     * @param <T>      The desired domain object type
     * @return The instance of the given type
     */
    @SneakyThrows
    protected <T> T handleResponse(Response response, JavaType type) {
        try (ResponseBody body = response.body()) {
            return body != null ? deserialize(body.string(), type) : null;
        }
    }

    @Override
    public void close() throws Exception {
        // OkHttp client does not need to be closed
    }

}
