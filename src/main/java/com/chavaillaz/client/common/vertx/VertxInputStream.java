package com.chavaillaz.client.common.vertx;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import io.vertx.core.buffer.Buffer;

/**
 * Input stream reading the content of a Vert.x {@link Buffer}.
 */
public class VertxInputStream extends InputStream {

    private final Buffer buffer;
    private int position = 0;

    /**
     * Creates a stream reading the given buffer from its start.
     *
     * @param buffer The buffer to read
     */
    public VertxInputStream(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public int read() throws IOException {
        if (position == buffer.length()) {
            return -1;
        }
        return buffer.getByte(position++) & 0xFF;
    }

    @Override
    public int read(byte[] buffer, int offset, int length) throws IOException {
        Objects.checkFromIndexSize(offset, length, buffer.length);
        if (length == 0) {
            return 0;
        }
        int size = Math.min(length, this.buffer.length() - position);
        if (size == 0) {
            return -1;
        }
        this.buffer.getBytes(position, position + size, buffer, offset);
        position += size;
        return size;
    }

}