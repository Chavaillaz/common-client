package com.chavaillaz.client.common.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("SerializationException")
class SerializationExceptionTest {

    @Test
    @DisplayName("Names the object it could not serialize, and its class")
    void testExceptionMessage() {
        Exception root = new Exception("Root");
        Exception exception = new SerializationException("Content", root);

        assertThat(exception)
                .hasMessageContainingAll("Content", "String")
                .cause().isSameAs(root);
    }

}
