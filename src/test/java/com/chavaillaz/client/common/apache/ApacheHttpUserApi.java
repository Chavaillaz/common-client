package com.chavaillaz.client.common.apache;

import static org.apache.hc.client5.http.async.methods.SimpleRequestBuilder.get;

import java.util.concurrent.CompletableFuture;

import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;

import com.chavaillaz.client.common.model.User;
import com.chavaillaz.client.common.model.UserApi;
import com.chavaillaz.client.common.security.Authentication;

public class ApacheHttpUserApi extends AbstractApacheHttpClient implements UserApi {

    public ApacheHttpUserApi(CloseableHttpAsyncClient client, String baseUrl, Authentication authentication) {
        super(client, baseUrl, authentication);
    }

    public CompletableFuture<User> getUser() {
        return sendAsync(requestBuilder(get(), URL_USER), User.class);
    }

}
