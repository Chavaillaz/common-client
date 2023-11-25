package com.chavaillaz.client.common.exception;

import lombok.Getter;

/**
 * Exception in case the request didn't return a success code.
 */
@Getter
public class ResponseException extends ClientException {

    private final Integer statusCode;
    private final String body;

    /**
     * Creates a new response exception.
     *
     * @param statusCode The status code returned
     * @param body       The content body
     */
    public ResponseException(int statusCode, String body) {
        this(statusCode, body, errorMessage(statusCode, body));
    }

    /**
     * Creates a new response exception.
     *
     * @param statusCode   The status code returned
     * @param body         The content body
     * @param errorMessage The extracted error message
     */
    public ResponseException(int statusCode, String body, String errorMessage) {
        super(errorMessage);
        this.statusCode = statusCode;
        this.body = body;
    }

    private static String errorMessage(Integer statusCode, String content) {
        return "Service responded with " + statusCode + ": " + content;
    }

}
