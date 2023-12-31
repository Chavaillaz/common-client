package com.chavaillaz.client.common.utility;

import static com.chavaillaz.client.common.utility.Utils.encodeBase64;
import static com.chavaillaz.client.common.utility.Utils.encodeQuery;
import static com.chavaillaz.client.common.utility.Utils.getCookieHeader;
import static com.chavaillaz.client.common.utility.Utils.getProperty;
import static com.chavaillaz.client.common.utility.Utils.readInputStream;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.BiConsumer;

import com.chavaillaz.client.common.security.Authentication;
import org.junit.jupiter.api.Test;

class UtilsTest {

    @Test
    void testReadInputStream() {
        String expected = "Test";
        InputStream stream = new ByteArrayInputStream(expected.getBytes(UTF_8));
        String result = readInputStream(stream);
        assertEquals(expected, result);
    }

    @Test
    void testEncodeQuery() {
        Map<Object, Object> map = new TreeMap<>();
        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);
        assertEquals("A=1&B=2&C=3", encodeQuery(map));
    }

    @Test
    void testEncodeBase64() {
        assertEquals("VGVzdA==", encodeBase64("Test"));
    }

    @Test
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
        assertTrue(cookieHeader.isPresent());
        assertEquals("FirstKey=FirstValue; SecondKey=SecondValue", cookieHeader.get());
    }

    @Test
    void testProperty() {
        System.setProperty("Test", "Value");
        assertNull(getProperty("Unknown"));
        assertEquals("Value", getProperty("Test"));
        assertEquals("Value", getProperty("Unknown", "Value"));
    }

}
