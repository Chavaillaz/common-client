package com.chavaillaz.client.common.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("AsynchronousException")
class AsynchronousExceptionTest {

    private static final Exception CAUSE = new IllegalStateException("Cause");

    @Test
    @DisplayName("Carries the message and the cause it is created with")
    void testConstructors() {
        assertThat(new AsynchronousException().getMessage()).isNull();
        assertThat(new AsynchronousException()).hasNoCause();
        assertThat(new AsynchronousException("Message"))
                .hasMessage("Message")
                .hasNoCause();
        assertThat(new AsynchronousException("Message", CAUSE))
                .hasMessage("Message")
                .cause().isSameAs(CAUSE);
    }

    @Test
    @DisplayName("Takes its message from the cause it is created with alone")
    void testCauseAlone() {
        assertThat(new AsynchronousException(CAUSE))
                .hasMessage(CAUSE.toString())
                .cause().isSameAs(CAUSE);
    }

}
