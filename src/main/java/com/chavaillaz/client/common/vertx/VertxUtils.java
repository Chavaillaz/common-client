package com.chavaillaz.client.common.vertx;

import java.util.Optional;

import com.chavaillaz.client.common.utility.ProxyConfiguration;
import io.vertx.core.net.ProxyOptions;
import io.vertx.ext.web.client.WebClientOptions;
import lombok.experimental.UtilityClass;

/**
 * Utilities for Vert.x Client.
 */
@UtilityClass
public class VertxUtils {

    /**
     * Creates new options for Vert.x web client with default configuration (30 seconds timeout).
     *
     * @param proxy The proxy configuration
     * @return The corresponding options
     */
    public static WebClientOptions newWebClientOptions(ProxyConfiguration proxy) {
        return new WebClientOptions()
                .setProxyOptions(Optional.ofNullable(proxy)
                        .map(config -> new ProxyOptions()
                                .setHost(config.getHost())
                                .setPort(config.getPort()))
                        .orElse(null))
                .setConnectTimeout(30_000)
                .setIdleTimeout(30_000);
    }

}
