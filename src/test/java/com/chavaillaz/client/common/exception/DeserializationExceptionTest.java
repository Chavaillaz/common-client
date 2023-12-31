package com.chavaillaz.client.common.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.jupiter.api.Test;

class DeserializationExceptionTest {

    @Test
    void testExceptionMessageWithClass() {
        Exception root = new Exception("Root");
        Exception exception = new DeserializationException("Content", String.class, root);

        assertTrue(exception.getMessage().contains("Content"));
        assertTrue(exception.getMessage().contains("String"));
        assertEquals(root, exception.getCause());
    }

    @Test
    void testExcepionMessageWithJavaType() {
        JavaType type = TypeFactory.defaultInstance()
                .constructSimpleType(String.class, new JavaType[]{});
        Exception root = new Exception("Root");
        Exception exception = new DeserializationException("Content", type, root);

        assertTrue(exception.getMessage().contains("Content"));
        assertTrue(exception.getMessage().contains("String"));
        assertEquals(root, exception.getCause());
    }

}
