package com.chavaillaz.client.common.exception;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.ConnectException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("RequestException")
class RequestExceptionTest {

    @Test
    @DisplayName("Names the method and the URL of the request that failed")
    void testExceptionMessage() {
        Exception cause = new ConnectException("Connection refused");

        assertThat(new RequestException("GET", "http://localhost:8080/user", cause))
                .hasMessage("Request GET http://localhost:8080/user failed")
                .cause().isSameAs(cause);
    }

}
