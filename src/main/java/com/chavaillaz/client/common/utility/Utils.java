package com.chavaillaz.client.common.utility;

import static java.net.URLEncoder.encode;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.chavaillaz.client.common.security.Authentication;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.tuple.Pair;

/**
 * General utilities for all clients.
 */
@UtilityClass
public class Utils {

    /**
     * Gets the content of an {@link InputStream} and convert it to a {@link String}.
     * Uses {@link StandardCharsets#UTF_8} as charset.
     *
     * @param inputStream The input stream to read
     * @return The {@link String} representing the content of the input stream
     * @throws RuntimeException If some I/O error occurs when reading the input stream
     */
    public static String readInputStream(InputStream inputStream) {
        return readInputStream(inputStream, StandardCharsets.UTF_8);
    }

    /**
     * Gets the content of an {@link InputStream} and convert it to a {@link String}.
     *
     * @param inputStream The input stream to read
     * @param charset     The charset to use to read the input stream
     * @return The {@link String} representing the content of the input stream
     * @throws RuntimeException If some I/O error occurs when reading the input stream
     */
    @SneakyThrows
    public static String readInputStream(InputStream inputStream, Charset charset) {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        for (int length; (length = inputStream.read(buffer)) != -1; ) {
            result.write(buffer, 0, length);
        }
        return result.toString(charset);
    }

    /**
     * Creates a query representing the given data.
     * For example, {@code Map.of("border", "orange", "background", "black")}
     * will be {@code border=orange&background=black}.
     *
     * @param data The data to format
     * @return the corresponding {@link String}
     */
    public static String encodeQuery(Map<Object, Object> data) {
        StringBuilder result = new StringBuilder();
        data.forEach((key, value) -> {
            if (!result.isEmpty()) {
                result.append("&");
            }
            String encodedName = encode(key.toString(), UTF_8);
            String encodedValue = encode(value.toString(), UTF_8);
            result.append(encodedName);
            if (encodedValue != null) {
                result.append("=");
                result.append(encodedValue);
            }
        });
        return result.toString();
    }

    /**
     * Encodes a {@link String} to base 64 format.
     *
     * @param value The value to encode
     * @return The encoded value
     */
    public static String encodeBase64(String value) {
        return Base64.getEncoder().encodeToString(value.getBytes());
    }

    /**
     * Gets the value of the {@code Cookie} header for the given authentication information.
     *
     * @param authentication The authentication to use
     * @return The optional header value
     */
    public static Optional<String> getCookieHeader(Authentication authentication) {
        List<Pair<String, String>> cookies = new ArrayList<>();
        authentication.fillCookies((key, value) -> cookies.add(Pair.of(key, value)));
        return getCookieHeader(cookies);
    }

    /**
     * Gets the value of the {@code Cookie} header for the given cookies.
     *
     * @param cookies The cookies with for each its key and value
     * @return The optional header value
     */
    public static Optional<String> getCookieHeader(List<Pair<String, String>> cookies) {
        if (cookies.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(cookies.stream()
                .map(pair -> pair.getKey() + "=" + pair.getValue())
                .collect(joining("; ")));
    }

    /**
     * Gets a property with priority from environment variable,
     * then from system properties or {@code null}.
     *
     * @param propertyKey The property key to search
     * @return The property value found (environment, system) or {@code null}
     */
    public static String getProperty(String propertyKey) {
        return getProperty(propertyKey, null);
    }

    /**
     * Gets a property with priority from environment variable,
     * then from system properties or defaulting to the given value.
     *
     * @param propertyKey  The property key to search
     * @param defaultValue The default value to use if no property was found
     * @return The property value found (environment, system) or the default given value
     */
    public static String getProperty(String propertyKey, String defaultValue) {
        return ofNullable(System.getenv(propertyKey))
                .or(() -> ofNullable(System.getProperty(propertyKey)))
                .orElse(defaultValue);
    }

}
