package com.chavaillaz.client.okhttp;

import static java.net.Proxy.Type.HTTP;
import static java.nio.file.Files.probeContentType;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.Duration;
import java.util.Optional;

import com.chavaillaz.client.utility.ProxyConfiguration;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

/**
 * Utilities for OkHttp Client.
 */
@UtilityClass
public class OkHttpUtils {

    /**
     * Creates a new asynchronous OkHttp HTTP client with default configuration (30 seconds timeout).
     *
     * @param proxy The proxy configuration
     * @return The corresponding client
     */
    public static OkHttpClient newHttpClient(ProxyConfiguration proxy) {
        return new OkHttpClient.Builder()
                .proxy(Optional.ofNullable(proxy)
                        .map(config -> new Proxy(HTTP, new InetSocketAddress(config.getHost(), config.getPort())))
                        .orElse(null))
                .connectTimeout(Duration.ofSeconds(30))
                .readTimeout(Duration.ofSeconds(30))
                .callTimeout(Duration.ofSeconds(0))
                .build();
    }

    /**
     * Generates a new multipart body with the given files.
     *
     * @param files The list of files to include
     * @return The multipart body
     */
    @SneakyThrows
    public static MultipartBody multipartWithFiles(File... files) {
        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        for (File file : files) {
            MediaType fileType = MediaType.parse(probeContentType(file.toPath()));
            multipartBuilder.addFormDataPart("file", file.getName(), RequestBody.create(file, fileType));
        }

        return multipartBuilder.build();
    }

}
