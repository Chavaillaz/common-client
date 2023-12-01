package com.chavaillaz.client.common.vertx;

import static java.nio.file.Files.probeContentType;

import java.io.File;
import java.util.Map;
import java.util.Optional;

import com.chavaillaz.client.common.utility.ProxyConfiguration;
import io.vertx.core.MultiMap;
import io.vertx.core.net.ProxyOptions;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.ext.web.multipart.MultipartForm;
import lombok.SneakyThrows;
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
    public static WebClientOptions defaultWebClientOptions(ProxyConfiguration proxy) {
        return new WebClientOptions()
                .setProxyOptions(Optional.ofNullable(proxy)
                        .map(config -> new ProxyOptions()
                                .setHost(config.getHost())
                                .setPort(config.getPort()))
                        .orElse(null))
                .setConnectTimeout(30_000)
                .setIdleTimeout(30_000);
    }

    /**
     * Creates a new multipart form with the given files.
     *
     * @param files The list of files to include
     * @return The multipart form
     */
    @SneakyThrows
    public static MultipartForm multipartWithFiles(File... files) {
        MultipartForm form = MultipartForm.create();
        for (File file : files) {
            form.binaryFileUpload("file", file.getName(), file.getAbsolutePath(), probeContentType(file.toPath()));
        }
        return form;
    }

    /**
     * Creates the HTTP parameters for form data using a map of key value representing the data to send.
     *
     * @param data The data to format and send as form data
     * @return The corresponding parameters
     */
    public MultiMap formData(Map<Object, Object> data) {
        MultiMap form = MultiMap.caseInsensitiveMultiMap();
        data.forEach((key, value) -> form.set(key.toString(), value.toString()));
        return form;
    }

}
