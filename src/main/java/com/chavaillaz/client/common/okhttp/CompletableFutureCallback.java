package com.chavaillaz.client.common.okhttp;

import static com.chavaillaz.client.common.okhttp.OkHttpUtils.getBodyOrError;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import com.chavaillaz.client.common.AbstractHttpClient;
import com.chavaillaz.client.common.exception.RequestException;
import com.chavaillaz.client.common.exception.ResponseException;

/**
 * Callback for OkHttp Client in order to transfer state to a {@link CompletableFuture}.
 * It completes exceptionally if the response code is not in the 2xx or 3xx range (see {@link ResponseException})
 * or if a connection failure happens (e.g. timeout).
 */
@Slf4j
public class CompletableFutureCallback implements Callback {

    private final AbstractHttpClient client;
    private final CompletableFuture<Response> future;

    /**
     * Creates a callback completing the given future with the response of the call, or failing it.
     *
     * @param client The client making the call, which builds the exception of an error response
     * @param future The future to complete
     */
    public CompletableFutureCallback(AbstractHttpClient client, CompletableFuture<Response> future) {
        this.client = client;
        this.future = future;
    }

    @Override
    public void onResponse(Call call, Response response) {
        log.debug("{} completed: {}", call.request(), response);

        if (response.code() >= 400) {
            future.completeExceptionally(getResponseException(call, response));
        } else {
            future.complete(response);
        }
    }

    private ResponseException getResponseException(Call call, Response response) {
        String content = getBodyOrError(response);
        String method = call.request().method();
        String url = call.request().url().uri().toString();
        return client.responseException(method, url, response.code(), content);
    }

    @Override
    public void onFailure(Call call, IOException exception) {
        log.debug("{} failed: {}", call.request(), exception.getMessage());
        future.completeExceptionally(getFailureException(call, exception));
    }

    private RequestException getFailureException(Call call, IOException exception) {
        String method = call.request().method();
        String url = call.request().url().uri().toString();
        return new RequestException(method, url, exception);
    }

}
