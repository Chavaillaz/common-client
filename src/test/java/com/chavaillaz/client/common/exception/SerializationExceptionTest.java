package com.chavaillaz.client.common.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class SerializationExceptionTest {

    @Test
    void testExceptionMessage() {
        Exception root = new Exception("Root");
        Exception exception = new SerializationException("Content", root);

        assertTrue(exception.getMessage().contains("Content"));
        assertTrue(exception.getMessage().contains("String"));
        assertEquals(root, exception.getCause());
    }

}
