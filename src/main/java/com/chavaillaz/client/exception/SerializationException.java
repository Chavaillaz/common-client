package com.chavaillaz.client.exception;

/**
 * Exception in case the body to be sent by the client cannot be serialized.
 */
public class SerializationException extends ClientException {

    /**
     * Creates a new serialization exception.
     *
     * @param object    The content to serialize
     * @param exception The exception thrown by Jackson
     */
    public SerializationException(Object object, Throwable exception) {
        super("Unable to serialize type " + object.getClass().getSimpleName() + " from " + object, exception);
    }

}
