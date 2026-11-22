package com.chavaillaz.client.common.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ClientException")
class ClientExceptionTest {

    private static final Exception CAUSE = new IllegalStateException("Cause");

    @Test
    @DisplayName("Carries the message and the cause it is created with")
    void testConstructors() {
        assertThat(new ClientException().getMessage()).isNull();
        assertThat(new ClientException()).hasNoCause();
        assertThat(new ClientException("Message"))
                .hasMessage("Message")
                .hasNoCause();
        assertThat(new ClientException("Message", CAUSE))
                .hasMessage("Message")
                .cause().isSameAs(CAUSE);
    }

    @Test
    @DisplayName("Takes its message from the cause it is created with alone")
    void testCauseAlone() {
        assertThat(new ClientException(CAUSE))
                .hasMessage(CAUSE.toString())
                .cause().isSameAs(CAUSE);
    }

}
