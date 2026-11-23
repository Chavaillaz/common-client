package com.chavaillaz.client.common.model;

import static com.chavaillaz.client.common.model.UserApi.URL_AVATAR;
import static com.chavaillaz.client.common.model.UserApi.URL_PREFERENCES;
import static com.chavaillaz.client.common.model.UserApi.URL_USER;
import static com.chavaillaz.client.common.model.UserApi.stubForUserApi;
import static com.github.tomakehurst.wiremock.client.WireMock.aMultipart;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.noContent;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.InstanceOfAssertFactories.type;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.CompletionException;
import java.util.function.BiConsumer;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.chavaillaz.client.common.exception.ResponseException;
import com.chavaillaz.client.common.security.AnonymousAuthentication;
import com.chavaillaz.client.common.security.Authentication;
import com.chavaillaz.client.common.utility.ProxyConfiguration;

/**
 * The scenarios every client plays against the same service, which WireMock stands for, each subclass giving the
 * {@link UserApi} of the client it tests.
 */
@WireMockTest
public abstract class AbstractUserApiTest {

    private static final byte[] AVATAR = {(byte) 0x89, 'P', 'N', 'G', 0, (byte) 0xFF};

    /**
     * Creates the API of users calling the service through the client tested.
     *
     * @param baseUrl        The base URL of the service
     * @param authentication The authentication of the requests
     * @param proxy          The proxy to call the service through, if any
     * @return The API of users
     */
    protected abstract UserApi newUserApi(String baseUrl, Authentication authentication, @Nullable ProxyConfiguration proxy);

    /**
     * Gives the exception the client fails with when nothing listens at the address of the service.
     *
     * @return The type of the exception
     */
    protected abstract Class<? extends Exception> unreachableException();

    /**
     * Gives the request sending the given entry of a form, which the clients encode as a URL query.
     *
     * @param name  The name of the entry
     * @param value The value of the entry
     * @return The pattern of the request
     */
    protected RequestPatternBuilder formRequest(String name, String value) {
        return postRequestedFor(urlEqualTo(URL_PREFERENCES))
                .withHeader("Content-Type", containing("application/x-www-form-urlencoded"))
                .withFormParam(name, equalTo(value));
    }

    @BeforeEach
    void setup() {
        stubForUserApi();
    }

    @Test
    @DisplayName("Gets the user the service answers with")
    void testGetUser(WireMockRuntimeInfo wiremock) throws IOException {
        try (UserApi api = newUserApi(wiremock.getHttpBaseUrl(), new AnonymousAuthentication(), null)) {
            assertThat(api.getUser().join())
                    .extracting(User::getUsername)
                    .isEqualTo("WireMock");
        }
    }

    @Test
    @DisplayName("Sends the body it serializes as JSON")
    void testUpdateUser(WireMockRuntimeInfo wiremock) throws IOException {
        stubFor(put(URL_USER)
                .withHeader("Content-Type", containing("application/json"))
                .withRequestBody(equalToJson("{\"username\": \"Updated\"}"))
                .willReturn(okJson("{\"username\": \"Updated\"}")));
        User user = new User();
        user.setUsername("Updated");

        try (UserApi api = newUserApi(wiremock.getHttpBaseUrl(), new AnonymousAuthentication(), null)) {
            assertThat(api.updateUser(user).join())
                    .extracting(User::getUsername)
                    .isEqualTo("Updated");
        }
    }

    @Test
    @DisplayName("Gets the body the service answers with as a stream of its bytes")
    void testGetAvatar(WireMockRuntimeInfo wiremock) throws IOException {
        stubFor(get(URL_AVATAR).willReturn(aResponse().withStatus(200).withBody(AVATAR)));

        try (UserApi api = newUserApi(wiremock.getHttpBaseUrl(), new AnonymousAuthentication(), null);
             InputStream avatar = api.getAvatar().join()) {
            assertThat(avatar).hasBinaryContent(AVATAR);
        }
    }

    @Test
    @DisplayName("Gets an empty stream when the service answers without content")
    void testGetNoAvatar(WireMockRuntimeInfo wiremock) throws IOException {
        stubFor(get(URL_AVATAR).willReturn(noContent()));

        try (UserApi api = newUserApi(wiremock.getHttpBaseUrl(), new AnonymousAuthentication(), null);
             InputStream avatar = api.getAvatar().join()) {
            assertThat(avatar).isEmpty();
        }
    }

    @Test
    @DisplayName("Completes with nothing when the service answers without content")
    void testDeleteUser(WireMockRuntimeInfo wiremock) throws IOException {
        stubFor(delete(URL_USER).willReturn(noContent()));

        try (UserApi api = newUserApi(wiremock.getHttpBaseUrl(), new AnonymousAuthentication(), null)) {
            assertThat(api.deleteUser().join()).isNull();
        }
    }

    @Test
    @DisplayName("Fails with the status and the body of an error response")
    void testErrorResponse(WireMockRuntimeInfo wiremock) throws IOException {
        stubFor(get(URL_USER).willReturn(aResponse().withStatus(404).withBody("Not Found")));

        try (UserApi api = newUserApi(wiremock.getHttpBaseUrl(), new AnonymousAuthentication(), null)) {
            CompletionException failure = assertThatExceptionOfType(CompletionException.class)
                    .isThrownBy(() -> api.getUser().join())
                    .actual();
            assertThat(failure.getCause())
                    .asInstanceOf(type(ResponseException.class))
                    .extracting(ResponseException::getStatusCode, ResponseException::getBody)
                    .containsExactly(404, "Not Found");
        }
    }

    @Test
    @DisplayName("Fails when nothing listens at the address of the service")
    void testUnreachable() throws IOException {
        int port;
        try (ServerSocket socket = new ServerSocket(0)) {
            port = socket.getLocalPort();
        }

        try (UserApi api = newUserApi("http://localhost:" + port, new AnonymousAuthentication(), null)) {
            assertThatExceptionOfType(CompletionException.class)
                    .isThrownBy(() -> api.getUser().join())
                    .withCauseInstanceOf(unreachableException());
        }
    }

    @Test
    @DisplayName("Sends the cookies the authentication fills")
    void testCookies(WireMockRuntimeInfo wiremock) throws IOException {
        stubFor(get(URL_USER)
                .withCookie("session", equalTo("abc"))
                .willReturn(okJson("{\"username\": \"Cookie\"}")));
        Authentication authentication = new AnonymousAuthentication() {

            @Override
            public void fillCookies(BiConsumer<String, String> addCookie) {
                addCookie.accept("session", "abc");
            }

        };

        try (UserApi api = newUserApi(wiremock.getHttpBaseUrl(), authentication, null)) {
            assertThat(api.getUser().join())
                    .extracting(User::getUsername)
                    .isEqualTo("Cookie");
        }
    }

    @Test
    @DisplayName("Calls the service through the proxy given")
    void testProxy(WireMockRuntimeInfo wiremock) throws IOException {
        ProxyConfiguration proxy = ProxyConfiguration.from("localhost", wiremock.getHttpPort());

        // No address resolves the host of the service: only the proxy, which WireMock stands for, answers
        try (UserApi api = newUserApi("http://users.invalid", new AnonymousAuthentication(), proxy)) {
            assertThat(api.getUser().join())
                    .extracting(User::getUsername)
                    .isEqualTo("WireMock");
        }
    }

    @Test
    @DisplayName("Sends the files of a multipart form")
    void testUploadAvatar(WireMockRuntimeInfo wiremock, @TempDir Path directory) throws IOException {
        stubFor(post(URL_AVATAR).willReturn(noContent()));
        File avatar = Files.writeString(directory.resolve("avatar.txt"), "Avatar").toFile();

        try (UserApi api = newUserApi(wiremock.getHttpBaseUrl(), new AnonymousAuthentication(), null)) {
            api.uploadAvatar(avatar).join();
        }

        verify(postRequestedFor(urlEqualTo(URL_AVATAR))
                .withAnyRequestBodyPart(aMultipart()
                        .withName("file")
                        .withBody(equalTo("Avatar"))));
    }

    @Test
    @DisplayName("Sends the entries of a form")
    void testUpdatePreferences(WireMockRuntimeInfo wiremock) throws IOException {
        stubFor(post(URL_PREFERENCES).willReturn(noContent()));

        try (UserApi api = newUserApi(wiremock.getHttpBaseUrl(), new AnonymousAuthentication(), null)) {
            api.updatePreferences(Map.of("theme", "dark mode")).join();
        }

        verify(formRequest("theme", "dark mode"));
    }

}
