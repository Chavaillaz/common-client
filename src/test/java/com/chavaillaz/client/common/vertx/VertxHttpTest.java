package com.chavaillaz.client.common.vertx;

import static com.chavaillaz.client.common.model.UserApi.stubForUserApi;
import static com.chavaillaz.client.common.vertx.VertxUtils.defaultWebClientOptions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.chavaillaz.client.common.model.User;
import com.chavaillaz.client.common.security.AnonymousAuthentication;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@WireMockTest
class VertxHttpTest {

    @BeforeEach
    void setup() {
        stubForUserApi();
    }

    @Test
    void testVertx(WireMockRuntimeInfo wiremock) throws Exception {
        WebClient client = WebClient.create(Vertx.vertx(), defaultWebClientOptions(null));
        try (var api = new VertxHttpUserApi(client, wiremock.getHttpBaseUrl(), new AnonymousAuthentication())) {
            User user = api.getUser().join();
            assertNotNull(user);
            assertEquals("WireMock", user.getUsername());
        }
    }

}
