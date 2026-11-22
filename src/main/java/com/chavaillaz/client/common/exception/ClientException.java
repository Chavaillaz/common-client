package com.chavaillaz.client.common.exception;

import java.io.Serial;

/**
 * General exception in case something fails in the client.
 */
public class ClientException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new generic client exception.
     */
    public ClientException() {
        super();
    }

    /**
     * Creates a new generic client exception.
     *
     * @param message The error message
     */
    public ClientException(String message) {
        super(message);
    }

    /**
     * Creates a new generic client exception.
     *
     * @param message The error message
     * @param cause   The root exception
     */
    public ClientException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new generic client exception.
     *
     * @param cause The root exception
     */
    public ClientException(Throwable cause) {
        super(cause);
    }

}
