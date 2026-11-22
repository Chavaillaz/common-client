package com.chavaillaz.client.common.apache;

import static com.chavaillaz.client.common.apache.ApacheHttpUtils.formData;
import static com.chavaillaz.client.common.apache.ApacheHttpUtils.multipartWithFiles;
import static org.apache.hc.client5.http.async.methods.SimpleRequestBuilder.delete;
import static org.apache.hc.client5.http.async.methods.SimpleRequestBuilder.get;
import static org.apache.hc.client5.http.async.methods.SimpleRequestBuilder.post;
import static org.apache.hc.client5.http.async.methods.SimpleRequestBuilder.put;
import static org.apache.hc.core5.http.ContentType.APPLICATION_JSON;

import java.io.File;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;

import com.chavaillaz.client.common.model.User;
import com.chavaillaz.client.common.model.UserApi;
import com.chavaillaz.client.common.security.Authentication;

public class ApacheHttpUserApi extends AbstractApacheHttpClient implements UserApi {

    public ApacheHttpUserApi(CloseableHttpAsyncClient client, String baseUrl, Authentication authentication) {
        super(client, baseUrl, authentication);
    }

    @Override
    public CompletableFuture<User> getUser() {
        return sendAsync(requestBuilder(get(), URL_USER), User.class);
    }

    @Override
    public CompletableFuture<InputStream> getAvatar() {
        return sendAsync(requestBuilder(get(), URL_AVATAR));
    }

    @Override
    public CompletableFuture<User> updateUser(User user) {
        return sendAsync(requestBuilder(put(), URL_USER).setBody(serialize(user), APPLICATION_JSON), User.class);
    }

    @Override
    public CompletableFuture<Void> deleteUser() {
        return sendAsync(requestBuilder(delete(), URL_USER), Void.class);
    }

    @Override
    public CompletableFuture<Void> uploadAvatar(File file) {
        return sendAsync(requestBuilder(post(), URL_AVATAR), multipartWithFiles(file), Void.class);
    }

    @Override
    public CompletableFuture<Void> updatePreferences(Map<Object, Object> preferences) {
        return sendAsync(requestBuilder(post(), URL_PREFERENCES)
                .setHeader(HEADER_CONTENT_TYPE, HEADER_CONTENT_FORM)
                .addParameters(formData(preferences)), Void.class);
    }

}
