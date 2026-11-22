package com.chavaillaz.client.common.okhttp;

import static com.chavaillaz.client.common.okhttp.AbstractOkHttpClient.MEDIA_TYPE_JSON;
import static com.chavaillaz.client.common.okhttp.OkHttpUtils.getBody;
import static com.chavaillaz.client.common.okhttp.OkHttpUtils.getBodyOrError;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("OkHttpUtils")
class OkHttpUtilsTest {

    private static Response response(@Nullable ResponseBody body) {
        return new Response.Builder()
                .request(new Request.Builder().url("http://localhost:8080/user").build())
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .body(body)
                .build();
    }

    @Test
    @DisplayName("Reads the body of a response, or nothing from a response without")
    void testGetBody() throws IOException {
        assertThat(getBody(response(ResponseBody.create("Content", MEDIA_TYPE_JSON)))).isEqualTo("Content");
        assertThat(getBody(response(null))).isNull();
    }

    @Test
    @DisplayName("Reads the body of a response, or the message of the failure reading it")
    void testGetBodyOrError() {
        assertThat(getBodyOrError(response(ResponseBody.create("Content", MEDIA_TYPE_JSON)))).isEqualTo("Content");
        assertThat(getBodyOrError(response(null))).isNull();
        assertThat(getBodyOrError(response(new FailingBody()))).isEqualTo("Connection reset");
    }

    /**
     * A body failing to be read, as when the connection is lost while reading it.
     */
    private static class FailingBody extends ResponseBody {

        @Override
        public @Nullable MediaType contentType() {
            return null;
        }

        @Override
        public long contentLength() {
            return -1;
        }

        @Override
        public BufferedSource source() {
            return Okio.buffer(new ForwardingSource(new Buffer()) {

                @Override
                public long read(Buffer sink, long byteCount) throws IOException {
                    throw new IOException("Connection reset");
                }

            });
        }

    }

}
