package com.chavaillaz.client.common.okhttp;

import static com.chavaillaz.client.common.okhttp.OkHttpUtils.formData;
import static com.chavaillaz.client.common.okhttp.OkHttpUtils.multipartWithFiles;

import java.io.File;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import okhttp3.OkHttpClient;

import com.chavaillaz.client.common.model.User;
import com.chavaillaz.client.common.model.UserApi;
import com.chavaillaz.client.common.security.Authentication;

public class OkHttpUserApi extends AbstractOkHttpClient implements UserApi {

    public OkHttpUserApi(OkHttpClient client, String baseUrl, Authentication authentication) {
        super(client, baseUrl, authentication);
    }

    @Override
    public CompletableFuture<User> getUser() {
        return sendAsync(requestBuilder(URL_USER).get(), User.class);
    }

    @Override
    public CompletableFuture<InputStream> getAvatar() {
        return sendAsync(requestBuilder(URL_AVATAR).get());
    }

    @Override
    public CompletableFuture<User> updateUser(User user) {
        return sendAsync(requestBuilder(URL_USER).put(body(user)), User.class);
    }

    @Override
    public CompletableFuture<Void> deleteUser() {
        return sendAsync(requestBuilder(URL_USER).delete(body()), Void.class);
    }

    @Override
    public CompletableFuture<Void> uploadAvatar(File file) {
        return sendAsync(requestBuilder(URL_AVATAR).post(multipartWithFiles(file)), Void.class);
    }

    @Override
    public CompletableFuture<Void> updatePreferences(Map<Object, Object> preferences) {
        return sendAsync(requestBuilder(URL_PREFERENCES).post(formData(preferences)), Void.class);
    }

}
