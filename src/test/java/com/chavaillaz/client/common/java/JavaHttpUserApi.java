package com.chavaillaz.client.common.java;

import java.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import com.chavaillaz.client.common.model.User;
import com.chavaillaz.client.common.model.UserApi;
import com.chavaillaz.client.common.security.Authentication;

public class JavaHttpUserApi extends AbstractJavaHttpClient implements UserApi {

    public JavaHttpUserApi(HttpClient client, String baseUrl, Authentication authentication) {
        super(client, baseUrl, authentication);
    }

    @Override
    public CompletableFuture<User> getUser() {
        return sendAsync(requestBuilder(URL_USER).GET(), User.class);
    }

}
