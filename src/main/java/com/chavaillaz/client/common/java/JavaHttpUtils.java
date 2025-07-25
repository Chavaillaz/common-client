package com.chavaillaz.client.common.java;

import java.io.File;
import java.io.IOException;
import java.net.CookieManager;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.http.HttpClient;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.chavaillaz.client.common.utility.ProxyConfiguration;
import com.chavaillaz.client.common.utility.Utils;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Utilities for Java HTTP Client.
 */
@UtilityClass
public class JavaHttpUtils {

    /**
     * Creates a new asynchronous Java HTTP client builder with default configuration (30 seconds timeout).
     *
     * @param proxy The proxy configuration
     * @return The corresponding client
     */
    public static HttpClient.Builder defaultHttpClientBuilder(ProxyConfiguration proxy) {
        return HttpClient.newBuilder()
                .cookieHandler(new CookieManager())
                .proxy(Optional.ofNullable(proxy)
                        .map(config -> ProxySelector.of(new InetSocketAddress(config.getHost(), config.getPort())))
                        .orElse(ProxySelector.getDefault()))
                .connectTimeout(Duration.ofSeconds(30));
    }

    /**
     * Creates a body publisher for form data using a map of key value representing the data to send.
     *
     * @param data The data to format and send as form data
     * @return The corresponding body publisher
     */
    public static BodyPublisher formData(Map<Object, Object> data) {
        return BodyPublishers.ofString(Utils.encodeQuery(data));
    }

    /**
     * Creates a body publisher for a multipart content.
     *
     * @param data     The data to store in the request
     * @param boundary The boundary to separate them
     * @param charset  The charset to use
     * @return The corresponding body publisher
     * @throws IOException if an error occurs when reading files if given in the data parameter
     */
    public static BodyPublisher mimeMultipartData(List<Pair<Object, Object>> data, String boundary, Charset charset) throws IOException {
        List<byte[]> byteArrays = new ArrayList<>();
        byte[] separator = ("--" + boundary + "\r\nContent-Disposition: form-data; name=").getBytes(charset);
        for (Pair<Object, Object> entry : data) {
            byteArrays.add(separator);

            // If value is type of Path (file) append content type with file name and file binaries, otherwise simply append key=value
            if (entry.getValue() instanceof Path path) {
                String mimeType = Files.probeContentType(path);
                byteArrays.add(("\"" + entry.getKey()
                        + "\"; filename=\"" + path.getFileName()
                        + "\"\r\nContent-Type: " + mimeType + "\r\n\r\n").getBytes(charset));
                byteArrays.add(Files.readAllBytes(path));
                byteArrays.add("\r\n".getBytes(charset));
            } else {
                byteArrays.add(("\"" + entry.getKey() + "\"\r\n\r\n" + entry.getValue() + "\r\n").getBytes(charset));
            }
        }

        byteArrays.add(("--" + boundary + "--").getBytes(charset));
        return BodyPublishers.ofByteArrays(byteArrays);
    }

    /**
     * Creates the {@link List} for {@link #mimeMultipartData(List, String, Charset)} with the given files.
     *
     * @param files The files
     * @return The filled list
     */
    public static List<Pair<Object, Object>> multipartWithFiles(File... files) {
        List<Pair<Object, Object>> data = new ArrayList<>();
        for (File file : files) {
            data.add(Pair.of("file", file.toPath()));
        }
        return data;
    }

}
