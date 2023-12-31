package com.chavaillaz.client.common.vertx;

import static com.chavaillaz.client.common.utility.Utils.readInputStream;
import static io.vertx.core.buffer.Buffer.buffer;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import io.vertx.core.buffer.Buffer;
import org.junit.jupiter.api.Test;

class VertxInputStreamTest {

    @Test
    void testReadInputStream() throws IOException {
        String expected = "Test";
        Buffer buffer = buffer(expected);

        try (VertxInputStream inputStream = new VertxInputStream(buffer)) {
            String result = readInputStream(inputStream);
            assertEquals(expected, result);
        }
    }

}
