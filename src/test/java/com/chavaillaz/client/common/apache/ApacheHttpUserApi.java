package com.chavaillaz.client.common.apache;

import static org.apache.hc.client5.http.async.methods.SimpleRequestBuilder.get;

import java.util.concurrent.CompletableFuture;

import com.chavaillaz.client.common.model.User;
import com.chavaillaz.client.common.model.UserApi;
import com.chavaillaz.client.common.security.Authentication;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;

public class ApacheHttpUserApi extends AbstractApacheHttpClient implements UserApi {

    public ApacheHttpUserApi(CloseableHttpAsyncClient client, String baseUrl, Authentication authentication) {
        super(client, baseUrl, authentication);
    }

    public CompletableFuture<User> getUser() {
        return sendAsync(requestBuilder(get(), URL_USER), User.class);
    }

}
