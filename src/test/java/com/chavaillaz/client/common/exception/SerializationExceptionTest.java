package com.chavaillaz.client.common.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SerializationExceptionTest {

    @Test
    void testExceptionMessage() {
        Exception root = new Exception("Root");
        Exception exception = new SerializationException("Content", root);

        assertThat(exception)
                .hasMessageContainingAll("Content", "String")
                .cause().isSameAs(root);
    }

}
