package com.chavaillaz.client.common.vertx;

import static io.vertx.core.http.HttpMethod.GET;

import java.util.concurrent.CompletableFuture;

import com.chavaillaz.client.common.model.User;
import com.chavaillaz.client.common.model.UserApi;
import com.chavaillaz.client.common.security.Authentication;
import io.vertx.ext.web.client.WebClient;

public class VertxHttpUserApi extends AbstractVertxHttpClient implements UserApi {

    protected VertxHttpUserApi(WebClient client, String baseUrl, Authentication authentication) {
        super(client, baseUrl, authentication);
    }

    @Override
    public CompletableFuture<User> getUser() {
        return handleAsync(requestBuilder(GET, URL_USER).send(), User.class);
    }

}
