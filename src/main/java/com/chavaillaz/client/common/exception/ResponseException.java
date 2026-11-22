package com.chavaillaz.client.common.exception;

import static org.apache.commons.lang3.StringUtils.SPACE;

import java.io.Serial;

import lombok.Getter;
import org.jspecify.annotations.Nullable;

/**
 * Exception in case the request didn't return a success code.
 */
@Getter
public class ResponseException extends ClientException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Integer statusCode;
    private final @Nullable String body;

    /**
     * Creates a new response exception.
     *
     * @param statusCode The status code returned
     * @param body       The content body
     */
    public ResponseException(int statusCode, @Nullable String body) {
        this(statusCode, body, errorMessage(statusCode, body));
    }

    /**
     * Creates a new response exception.
     *
     * @param method     The request HTTP method
     * @param url        The request URL
     * @param statusCode The status code returned
     * @param body       The content body
     */
    public ResponseException(String method, String url, int statusCode, @Nullable String body) {
        this(statusCode, body, errorMessage(statusCode, body) + " for " + method + SPACE + url);
    }

    /**
     * Creates a new response exception.
     *
     * @param statusCode   The status code returned
     * @param body         The content body
     * @param errorMessage The extracted error message
     */
    public ResponseException(int statusCode, @Nullable String body, String errorMessage) {
        super(errorMessage);
        this.statusCode = statusCode;
        this.body = body;
    }

    private static String errorMessage(Integer statusCode, @Nullable String content) {
        return "Service responded with " + statusCode + ": " + content;
    }

}
