package com.chavaillaz.client.common.vertx;

import static com.chavaillaz.client.common.utility.Utils.readInputStream;
import static io.vertx.core.buffer.Buffer.buffer;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import io.vertx.core.buffer.Buffer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("VertxInputStream")
class VertxInputStreamTest {

    @Test
    @DisplayName("Reads the whole buffer")
    void testReadInputStream() throws IOException {
        String expected = "Test";
        Buffer buffer = buffer(expected);

        try (VertxInputStream inputStream = new VertxInputStream(buffer)) {
            String result = readInputStream(inputStream);
            assertThat(result).isEqualTo(expected);
        }
    }

}
