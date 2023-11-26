package com.chavaillaz.client.common.apache;

import static org.apache.hc.core5.http.ContentType.DEFAULT_BINARY;

import java.io.File;
import java.util.Optional;
import java.util.stream.Stream;

import com.chavaillaz.client.common.utility.ProxyConfiguration;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.entity.mime.FileBody;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClientBuilder;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManagerBuilder;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.util.Timeout;

/**
 * Utilities for Apache HTTP Client.
 */
@UtilityClass
public class ApacheHttpUtils {

    /**
     * Creates a new asynchronous Apache HTTP client with default configuration (30 seconds timeout).
     *
     * @param proxy The proxy configuration
     * @return The corresponding client
     */
    public static CloseableHttpAsyncClient newHttpClient(ProxyConfiguration proxy) {
        return HttpAsyncClientBuilder.create()
                .useSystemProperties()
                .setDefaultCookieStore(new BasicCookieStore())
                .setProxy(Optional.ofNullable(proxy)
                        .map(config -> new HttpHost(config.getScheme(), config.getHost(), config.getPort()))
                        .orElse(null))
                .setConnectionManager(PoolingAsyncClientConnectionManagerBuilder.create()
                        .setDefaultConnectionConfig(ConnectionConfig.custom()
                                .setConnectTimeout(Timeout.ofSeconds(30))
                                .setSocketTimeout(null)
                                .build())
                        .build())
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setResponseTimeout(null)
                        .build())
                .build();
    }

    /**
     * Generates a new multipart entity builder with the given files.
     *
     * @param files The list of files to include
     * @return The multipart entity builder
     */
    @SneakyThrows
    public static MultipartEntityBuilder multipartWithFiles(File... files) {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create()
                .setContentType(DEFAULT_BINARY)
                .setStrictMode();
        Stream.of(files)
                .map(FileBody::new)
                .forEach(body -> builder.addPart("file", body));
        return builder;
    }

}
