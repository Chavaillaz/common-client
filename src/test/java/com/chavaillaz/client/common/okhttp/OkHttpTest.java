package com.chavaillaz.client.common.okhttp;

import static com.chavaillaz.client.common.model.UserApi.stubForUserApi;
import static com.chavaillaz.client.common.okhttp.OkHttpUtils.defaultHttpClientBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.chavaillaz.client.common.model.User;
import com.chavaillaz.client.common.security.AnonymousAuthentication;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@WireMockTest
class OkHttpTest {

    @BeforeEach
    void setup() {
        stubForUserApi();
    }

    @Test
    void testOkHttp(WireMockRuntimeInfo wiremock) throws Exception {
        OkHttpClient client = defaultHttpClientBuilder(null).build();
        try (var api = new OkHttpUserApi(client, wiremock.getHttpBaseUrl(), new AnonymousAuthentication())) {
            User user = api.getUser().join();
            assertNotNull(user);
            assertEquals("WireMock", user.getUsername());
        }
    }

}