package com.chavaillaz.client.common.exception;

import static org.apache.commons.lang3.StringUtils.SPACE;

/**
 * Exception in case the request fails before getting a response from the server.
 */
public class RequestException extends ClientException {

    /**
     * Creates a new request exception.
     *
     * @param method    The request HTTP method
     * @param url       The request URL
     * @param exception The exception thrown
     */
    public RequestException(String method, String url, Throwable exception) {
        super("Request " + method + SPACE + url + " failed", exception);
    }

}
