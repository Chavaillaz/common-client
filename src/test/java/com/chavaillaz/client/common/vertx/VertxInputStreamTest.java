package com.chavaillaz.client.common.vertx;

import static com.chavaillaz.client.common.utility.Utils.readInputStream;
import static io.vertx.core.buffer.Buffer.buffer;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIndexOutOfBoundsException;

import java.io.IOException;
import java.util.Random;

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

    @Test
    @DisplayName("Reads no more bytes than asked for, from the offset given")
    void testReadLengthAtOffset() throws IOException {
        byte[] target = new byte[4];

        try (VertxInputStream inputStream = new VertxInputStream(buffer("Test"))) {
            assertThat(inputStream.read(target, 1, 2)).isEqualTo(2);
            assertThat(target).containsExactly(0, 'T', 'e', 0);

            assertThat(inputStream.read(target, 0, 4)).isEqualTo(2);
            assertThat(target).containsExactly('s', 't', 'e', 0);

            assertThat(inputStream.read(target, 0, 4)).isEqualTo(-1);
        }
    }

    @Test
    @DisplayName("Reads all the bytes of a buffer larger than the chunks readAllBytes reads")
    void testReadAllBytes() throws IOException {
        byte[] content = new byte[20_000];
        new Random(42).nextBytes(content);

        try (VertxInputStream inputStream = new VertxInputStream(Buffer.buffer(content))) {
            assertThat(inputStream.readAllBytes()).isEqualTo(content);
        }
    }

    @Test
    @DisplayName("Reads nothing when asked for no byte")
    void testReadNoByte() throws IOException {
        try (VertxInputStream inputStream = new VertxInputStream(buffer("Test"))) {
            assertThat(inputStream.read(new byte[4], 0, 0)).isZero();
            assertThat(inputStream.read()).isEqualTo('T');
        }
    }

    @Test
    @DisplayName("Refuses to read beyond the end of the array given")
    void testReadBeyondArray() throws IOException {
        try (VertxInputStream inputStream = new VertxInputStream(buffer("Test"))) {
            assertThatIndexOutOfBoundsException()
                    .isThrownBy(() -> inputStream.read(new byte[4], 2, 3));
        }
    }

}
