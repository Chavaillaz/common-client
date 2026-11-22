package com.chavaillaz.client.common.utility;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.math.NumberUtils.toInt;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Data;
import org.jspecify.annotations.Nullable;

/**
 * Proxy configuration to be used in order to access the desired services.
 */
@Data
public class ProxyConfiguration {

    public static final Pattern PROXY_PATTERN = Pattern.compile("(https?):\\/\\/(.*):(\\d+)");

    private final String host;
    private final Integer port;
    private final @Nullable String scheme;

    /**
     * Creates a new proxy configuration.
     *
     * @param scheme The proxy scheme
     * @param host   The proxy host
     * @param port   The proxy port
     */
    protected ProxyConfiguration(@Nullable String scheme, String host, Integer port) {
        this.scheme = scheme;
        this.host = host;
        this.port = port;
    }

    /**
     * Creates a new proxy configuration from host and port.
     * Note that if the host or the port is {@code null}, the returned value will be {@code null}.
     *
     * @param host The proxy host
     * @param port The proxy port
     * @return The corresponding proxy configuration or {@code null}
     */
    public static @Nullable ProxyConfiguration from(@Nullable String host, @Nullable Integer port) {
        return from(null, host, port);
    }

    /**
     * Creates a new proxy configuration from scheme, host and port.
     * Note that if the host or the port is {@code null}, the returned value will be {@code null}.
     *
     * @param scheme The proxy scheme
     * @param host   The proxy host
     * @param port   The proxy port
     * @return The corresponding proxy configuration or {@code null}
     */
    public static @Nullable ProxyConfiguration from(@Nullable String scheme, @Nullable String host, @Nullable Integer port) {
        if (isNotBlank(host) && nonNull(port)) {
            return new ProxyConfiguration(scheme, host, port);
        }
        return null;
    }

    /**
     * Creates a new proxy configuration from a URL.
     * Note that if the path is not valid, the returned value will be {@code null}.
     *
     * @param path The proxy URL
     * @return The corresponding proxy configuration or {@code null}
     */
    public static @Nullable ProxyConfiguration from(String path) {
        Matcher matcher = PROXY_PATTERN.matcher(path);
        if (matcher.find()) {
            return new ProxyConfiguration(matcher.group(1), matcher.group(2), toInt(matcher.group(3)));
        }
        return null;
    }

    /**
     * Gets the full URL with scheme, host and port.
     *
     * @return The full proxy URL
     */
    public String getUrl() {
        StringBuilder url = new StringBuilder();
        if (scheme != null) {
            url.append(scheme);
            url.append("://");
        }
        url.append(host);
        if (port != null) {
            url.append(":");
            url.append(port);
        }
        return url.toString();
    }

}