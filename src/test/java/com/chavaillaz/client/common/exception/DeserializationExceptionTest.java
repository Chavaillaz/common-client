package com.chavaillaz.client.common.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("DeserializationException")
class DeserializationExceptionTest {

    @Test
    @DisplayName("Names the content and the class it could not be deserialized as")
    void testExceptionMessageWithClass() {
        Exception root = new Exception("Root");
        Exception exception = new DeserializationException("Content", String.class, root);

        assertThat(exception)
                .hasMessageContainingAll("Content", "String")
                .cause().isSameAs(root);
    }

    @Test
    @DisplayName("Names the content and the Java type it could not be deserialized as")
    void testExceptionMessageWithJavaType() {
        JavaType type = TypeFactory.defaultInstance()
                .constructSimpleType(String.class, new JavaType[]{});
        Exception root = new Exception("Root");
        Exception exception = new DeserializationException("Content", type, root);

        assertThat(exception)
                .hasMessageContainingAll("Content", "String")
                .cause().isSameAs(root);
    }

}
