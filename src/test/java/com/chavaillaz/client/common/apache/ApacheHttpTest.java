package com.chavaillaz.client.common.apache;

import static com.chavaillaz.client.common.apache.ApacheHttpUtils.defaultHttpClientBuilder;
import static com.chavaillaz.client.common.model.UserApi.stubForUserApi;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.chavaillaz.client.common.model.User;
import com.chavaillaz.client.common.security.AnonymousAuthentication;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@WireMockTest
class ApacheHttpTest {

    @BeforeEach
    void setup() {
        stubForUserApi();
    }

    @Test
    void testApache(WireMockRuntimeInfo wiremock) throws Exception {
        CloseableHttpAsyncClient client = defaultHttpClientBuilder(null).build();
        try (var api = new ApacheHttpUserApi(client, wiremock.getHttpBaseUrl(), new AnonymousAuthentication())) {
            User user = api.getUser().join();
            assertNotNull(user);
            assertEquals("WireMock", user.getUsername());
        }
    }

}
