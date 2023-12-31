package com.chavaillaz.client.common.okhttp;

import java.util.concurrent.CompletableFuture;

import com.chavaillaz.client.common.model.User;
import com.chavaillaz.client.common.model.UserApi;
import com.chavaillaz.client.common.security.Authentication;
import okhttp3.OkHttpClient;

public class OkHttpUserApi extends AbstractOkHttpClient implements UserApi {

    public OkHttpUserApi(OkHttpClient client, String baseUrl, Authentication authentication) {
        super(client, baseUrl, authentication);
    }

    @Override
    public CompletableFuture<User> getUser() {
        return sendAsync(requestBuilder(URL_USER).get(), User.class);
    }

}
