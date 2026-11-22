package com.chavaillaz.client.common.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ResponseExceptionTest {

    @Test
    void testExceptionMessage() {
        ResponseException exception = new ResponseException(404, "Not Found");
        assertThat(exception.getStatusCode()).isEqualTo(404);
        assertThat(exception.getBody()).isEqualTo("Not Found");
        assertThat(exception)
                .hasMessageContainingAll("404", "Not Found")
                .hasNoCause();
    }

}
