package com.chavaillaz.client.common.apache;

import java.util.concurrent.CompletableFuture;

import com.chavaillaz.client.common.AbstractHttpClient;
import com.chavaillaz.client.common.exception.RequestException;
import com.chavaillaz.client.common.exception.ResponseException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.core5.concurrent.FutureCallback;

/**
 * Future callback for Apache HTTP Client in order to transfer state to a {@link CompletableFuture}.
 * It completes exceptionally if the response code is not in the 2xx or 3xx range (see {@link ResponseException})
 * or if a connection failure happens (e.g. timeout).
 */
@Slf4j
@AllArgsConstructor
public class CompletableFutureCallback implements FutureCallback<SimpleHttpResponse> {

    private final AbstractHttpClient client;
    private final SimpleHttpRequest request;
    private final CompletableFuture<SimpleHttpResponse> future;

    @Override
    public void completed(SimpleHttpResponse response) {
        log.debug("Request {} completed: {}", request, response);
        if (response.getCode() >= 400) {
            future.completeExceptionally(
                    client.responseException(
                            request.getMethod(),
                            request.getRequestUri(),
                            response.getCode(),
                            response.getBodyText()));
        } else {
            future.complete(response);
        }
    }

    @Override
    public void failed(Exception exception) {
        log.debug("Request {} failed: {}", request, exception.getMessage());
        future.completeExceptionally(
                new RequestException(
                        request.getMethod(),
                        request.getRequestUri(),
                        exception));
    }

    @Override
    public void cancelled() {
        log.debug("Request {} cancelled", request);
        future.cancel(false);
    }

}
