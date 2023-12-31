package com.chavaillaz.client.common.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ResponseExceptionTest {

    @Test
    void testExceptionMessage() {
        ResponseException exception = new ResponseException(404, "Not Found");
        assertEquals(404, exception.getStatusCode());
        assertEquals("Not Found", exception.getBody());
        assertTrue(exception.getMessage().contains("404"));
        assertTrue(exception.getMessage().contains("Not Found"));
        assertNull(exception.getCause());
    }

}
