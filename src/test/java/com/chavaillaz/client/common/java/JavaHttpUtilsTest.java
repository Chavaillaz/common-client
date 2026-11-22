package com.chavaillaz.client.common.java;

import static com.chavaillaz.client.common.java.JavaHttpUtils.mimeMultipartData;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.http.HttpRequest.BodyPublisher;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

@DisplayName("JavaHttpUtils")
class JavaHttpUtilsTest {

    @Test
    @DisplayName("Gives the length of the multipart body it builds")
    void testMultipartLength(@TempDir Path directory) throws IOException {
        Path file = Files.writeString(directory.resolve("avatar.txt"), "Avatar");
        List<Pair<Object, Object>> data = List.of(Pair.of("file", file), Pair.of("comment", "New avatar"));

        BodyPublisher body = mimeMultipartData(data, "Boundary", UTF_8);

        String expected = "--Boundary\r\n"
                + "Content-Disposition: form-data; name=\"file\"; filename=\"avatar.txt\"\r\n"
                + "Content-Type: " + Files.probeContentType(file) + "\r\n\r\n"
                + "Avatar\r\n"
                + "--Boundary\r\n"
                + "Content-Disposition: form-data; name=\"comment\"\r\n\r\n"
                + "New avatar\r\n"
                + "--Boundary--";
        assertThat(body.contentLength()).isEqualTo(expected.getBytes(UTF_8).length);
    }

}
