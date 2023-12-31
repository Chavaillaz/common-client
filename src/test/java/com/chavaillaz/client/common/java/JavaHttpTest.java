package com.chavaillaz.client.common.java;

import static com.chavaillaz.client.common.java.JavaHttpUtils.defaultHttpClientBuilder;
import static com.chavaillaz.client.common.model.UserApi.stubForUserApi;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.http.HttpClient;

import com.chavaillaz.client.common.model.User;
import com.chavaillaz.client.common.security.AnonymousAuthentication;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@WireMockTest
class JavaHttpTest {

    @BeforeEach
    void setup() {
        stubForUserApi();
    }

    @Test
    void testJava(WireMockRuntimeInfo wiremock) {
        HttpClient client = defaultHttpClientBuilder(null).build();
        try (var api = new JavaHttpUserApi(client, wiremock.getHttpBaseUrl(), new AnonymousAuthentication())) {
            User user = api.getUser().join();
            assertNotNull(user);
            assertEquals("WireMock", user.getUsername());
        }
    }

}
