package com.chavaillaz.client.common.vertx;

import static com.chavaillaz.client.common.vertx.VertxUtils.defaultWebClientOptions;

import java.net.ConnectException;

import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;

import com.chavaillaz.client.common.model.AbstractUserApiTest;
import com.chavaillaz.client.common.model.UserApi;
import com.chavaillaz.client.common.security.Authentication;
import com.chavaillaz.client.common.utility.ProxyConfiguration;

@DisplayName("AbstractVertxHttpClient")
class VertxHttpTest extends AbstractUserApiTest {

    private static final Vertx VERTX = Vertx.vertx();

    @AfterAll
    static void closeVertx() {
        VERTX.close().toCompletionStage().toCompletableFuture().join();
    }

    @Override
    protected UserApi newUserApi(String baseUrl, Authentication authentication, @Nullable ProxyConfiguration proxy) {
        return new VertxHttpUserApi(WebClient.create(VERTX, defaultWebClientOptions(proxy)), baseUrl, authentication);
    }

    @Override
    protected Class<? extends Exception> unreachableException() {
        return ConnectException.class;
    }

}
