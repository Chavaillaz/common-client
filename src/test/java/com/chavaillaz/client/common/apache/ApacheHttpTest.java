package com.chavaillaz.client.common.apache;

import static com.chavaillaz.client.common.apache.ApacheHttpUtils.defaultHttpClientBuilder;
import static com.chavaillaz.client.common.model.UserApi.URL_USER;
import static com.chavaillaz.client.common.model.UserApi.stubForUserApi;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.common.ContentTypes.APPLICATION_JSON;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiConsumer;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.chavaillaz.client.common.model.User;
import com.chavaillaz.client.common.security.AnonymousAuthentication;
import com.chavaillaz.client.common.security.Authentication;

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
            assertThat(user).isNotNull();
            assertThat(user.getUsername()).isEqualTo("WireMock");
        }
    }

    @Test
    void testApacheCookies(WireMockRuntimeInfo wiremock) throws Exception {
        // Given: an authentication filling a cookie, for which the service answers with a user of its own
        stubFor(get(URL_USER).withCookie("session", equalTo("abc")).willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", APPLICATION_JSON)
                .withBody("{\"username\": \"Cookie\"}")));
        Authentication authentication = new AnonymousAuthentication() {

            @Override
            public void fillCookies(BiConsumer<String, String> addCookie) {
                addCookie.accept("session", "abc");
            }

        };

        // When
        CloseableHttpAsyncClient client = defaultHttpClientBuilder(null).build();
        try (var api = new ApacheHttpUserApi(client, wiremock.getHttpBaseUrl(), authentication)) {
            User user = api.getUser().join();

            // Then: the request carried the cookie
            assertThat(user.getUsername()).isEqualTo("Cookie");
        }
    }

}
