package com.chavaillaz.client.common.okhttp;

import static java.net.Proxy.Type.HTTP;
import static java.nio.file.Files.probeContentType;
import static okhttp3.MultipartBody.FORM;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;

import com.chavaillaz.client.common.utility.ProxyConfiguration;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Utilities for OkHttp Client.
 */
@UtilityClass
public class OkHttpUtils {

    /**
     * Creates a new asynchronous OkHttp HTTP client builder with default configuration (30 seconds timeout).
     *
     * @param proxy The proxy configuration
     * @return The corresponding client
     */
    public static OkHttpClient.Builder defaultHttpClientBuilder(ProxyConfiguration proxy) {
        return new OkHttpClient.Builder()
                .proxy(Optional.ofNullable(proxy)
                        .map(config -> new Proxy(HTTP, new InetSocketAddress(config.getHost(), config.getPort())))
                        .orElse(null))
                .connectTimeout(Duration.ofSeconds(30))
                .readTimeout(Duration.ofSeconds(30))
                .callTimeout(Duration.ofSeconds(0));
    }

    /**
     * Creates a new multipart body with the given files.
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

    /**
     * Creates the request body for form data using a map of key value representing the data to send.
     *
     * @param data The data to format and send as form data
     * @return The corresponding request body
     */
    public RequestBody formData(Map<Object, Object> data) {
        MultipartBody.Builder form = new MultipartBody.Builder().setType(FORM);
        data.forEach((key, value) -> form.addFormDataPart(key.toString(), value.toString()));
        return form.build();
    }

    /**
     * Gets the body of the given response.
     * Note that the body can only be read once.
     *
     * @param response The HTTP response
     * @return The body content or {@code null} if not present
     * @throws IOException If an error occurs when reading the body content
     */
    public String getBody(Response response) throws IOException {
        try (ResponseBody body = response.body()) {
            return body != null ? body.string() : null;
        }
    }

    /**
     * Gets the body of the given response or the exception message in case of error.
     * Note that the body can only be read once.
     *
     * @param response The HTTP response
     * @return The body content or the exception message when reading it
     */
    public String getBodyOrError(Response response) {
        try (ResponseBody body = response.body()) {
            return body != null ? body.string() : null;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

}
