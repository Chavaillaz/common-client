package com.chavaillaz.client.common.exception;

/**
 * Exception in case an error occurs during an asynchronous processing
 * when chaining actions in a {@link java.util.concurrent.CompletableFuture}
 * without waiting the result of it.
 */
public class AsynchronousException extends RuntimeException {

    /**
     * Creates a new asynchronous exception.
     */
    public AsynchronousException() {
        super();
    }

    /**
     * Creates a new asynchronous exception.
     *
     * @param message The error message
     */
    public AsynchronousException(String message) {
        super(message);
    }

    /**
     * Creates a new asynchronous exception.
     *
     * @param message The error message
     * @param cause   The root exception
     */
    public AsynchronousException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new asynchronous exception.
     *
     * @param cause The root exception
     */
    public AsynchronousException(Throwable cause) {
        super(cause);
    }

}
