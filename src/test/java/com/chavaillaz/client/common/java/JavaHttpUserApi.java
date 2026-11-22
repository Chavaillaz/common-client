package com.chavaillaz.client.common.java;

import static com.chavaillaz.client.common.java.JavaHttpUtils.formData;
import static com.chavaillaz.client.common.java.JavaHttpUtils.mimeMultipartData;
import static com.chavaillaz.client.common.java.JavaHttpUtils.multipartWithFiles;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpClient;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.chavaillaz.client.common.model.User;
import com.chavaillaz.client.common.model.UserApi;
import com.chavaillaz.client.common.security.Authentication;

public class JavaHttpUserApi extends AbstractJavaHttpClient implements UserApi {

    private static final String BOUNDARY = "UserApiBoundary";

    public JavaHttpUserApi(HttpClient client, String baseUrl, Authentication authentication) {
        super(client, baseUrl, authentication);
    }

    @Override
    public CompletableFuture<User> getUser() {
        return sendAsync(requestBuilder(URL_USER).GET(), User.class);
    }

    @Override
    public CompletableFuture<InputStream> getAvatar() {
        return sendAsync(requestBuilder(URL_AVATAR).GET());
    }

    @Override
    public CompletableFuture<User> updateUser(User user) {
        return sendAsync(requestBuilder(URL_USER).PUT(body(user)), User.class);
    }

    @Override
    public CompletableFuture<Void> deleteUser() {
        return sendAsync(requestBuilder(URL_USER).DELETE(), Void.class);
    }

    @Override
    public CompletableFuture<Void> uploadAvatar(File file) throws IOException {
        return sendAsync(requestBuilder(URL_AVATAR)
                .setHeader(HEADER_CONTENT_TYPE, "multipart/form-data; boundary=" + BOUNDARY)
                .POST(mimeMultipartData(multipartWithFiles(file), BOUNDARY, UTF_8)), Void.class);
    }

    @Override
    public CompletableFuture<Void> updatePreferences(Map<Object, Object> preferences) {
        return sendAsync(requestBuilder(URL_PREFERENCES)
                .setHeader(HEADER_CONTENT_TYPE, HEADER_CONTENT_FORM)
                .POST(formData(preferences)), Void.class);
    }

}
