package com.chavaillaz.client.common.vertx;

import static com.chavaillaz.client.common.vertx.VertxUtils.formData;
import static com.chavaillaz.client.common.vertx.VertxUtils.multipartWithFiles;
import static io.vertx.core.http.HttpMethod.DELETE;
import static io.vertx.core.http.HttpMethod.GET;
import static io.vertx.core.http.HttpMethod.POST;
import static io.vertx.core.http.HttpMethod.PUT;

import java.io.File;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import io.vertx.ext.web.client.WebClient;

import com.chavaillaz.client.common.model.User;
import com.chavaillaz.client.common.model.UserApi;
import com.chavaillaz.client.common.security.Authentication;

public class VertxHttpUserApi extends AbstractVertxHttpClient implements UserApi {

    protected VertxHttpUserApi(WebClient client, String baseUrl, Authentication authentication) {
        super(client, baseUrl, authentication);
    }

    @Override
    public CompletableFuture<User> getUser() {
        return handleAsync(requestBuilder(GET, URL_USER).send(), User.class);
    }

    @Override
    public CompletableFuture<InputStream> getAvatar() {
        return handleAsync(requestBuilder(GET, URL_AVATAR).send());
    }

    @Override
    public CompletableFuture<User> updateUser(User user) {
        return handleAsync(requestBuilder(PUT, URL_USER).sendBuffer(body(user)), User.class);
    }

    @Override
    public CompletableFuture<Void> deleteUser() {
        return handleAsync(requestBuilder(DELETE, URL_USER).send(), Void.class);
    }

    @Override
    public CompletableFuture<Void> uploadAvatar(File file) {
        return handleAsync(requestBuilder(POST, URL_AVATAR)
                .putHeader(HEADER_CONTENT_TYPE, "multipart/form-data")
                .sendMultipartForm(multipartWithFiles(file)), Void.class);
    }

    @Override
    public CompletableFuture<Void> updatePreferences(Map<Object, Object> preferences) {
        return handleAsync(requestBuilder(POST, URL_PREFERENCES)
                .putHeader(HEADER_CONTENT_TYPE, HEADER_CONTENT_FORM)
                .sendForm(formData(preferences)), Void.class);
    }

}
