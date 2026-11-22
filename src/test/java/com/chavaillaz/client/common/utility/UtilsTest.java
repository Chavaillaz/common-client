package com.chavaillaz.client.common.utility;

import static com.chavaillaz.client.common.utility.Utils.encodeBase64;
import static com.chavaillaz.client.common.utility.Utils.encodeQuery;
import static com.chavaillaz.client.common.utility.Utils.getCookieHeader;
import static com.chavaillaz.client.common.utility.Utils.getProperty;
import static com.chavaillaz.client.common.utility.Utils.readInputStream;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.BiConsumer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.chavaillaz.client.common.security.Authentication;

@DisplayName("Utils")
class UtilsTest {

    @Test
    @DisplayName("Reads a stream as UTF-8 text")
    void testReadInputStream() throws IOException {
        String expected = "Test";
        InputStream stream = new ByteArrayInputStream(expected.getBytes(UTF_8));
        String result = readInputStream(stream);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("Encodes the entries of a map as a query string")
    void testEncodeQuery() {
        Map<Object, Object> map = new TreeMap<>();
        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);
        assertThat(encodeQuery(map)).isEqualTo("A=1&B=2&C=3");
    }

    @Test
    @DisplayName("Encodes a value in Base64")
    void testEncodeBase64() {
        assertThat(encodeBase64("Test")).isEqualTo("VGVzdA==");
    }

    @Test
    @DisplayName("Joins the cookies of an authentication into one header")
    void testCookieHeaders() {
        Authentication authentication = new Authentication() {

            @Override
            public void fillHeaders(BiConsumer<String, String> addHeader) {
                // Nothing to do
            }

            @Override
            public void fillCookies(BiConsumer<String, String> addCookie) {
                addCookie.accept("FirstKey", "FirstValue");
                addCookie.accept("SecondKey", "SecondValue");
            }

        };

        Optional<String> cookieHeader = getCookieHeader(authentication);
        assertThat(cookieHeader).contains("FirstKey=FirstValue; SecondKey=SecondValue");
    }

    @Test
    @DisplayName("Reads a property from the environment or the system, or falls back to a default")
    void testProperty() {
        System.setProperty("Test", "Value");
        assertThat(getProperty("Unknown")).isNull();
        assertThat(getProperty("Test")).isEqualTo("Value");
        assertThat(getProperty("Unknown", "Value")).isEqualTo("Value");
    }

}
