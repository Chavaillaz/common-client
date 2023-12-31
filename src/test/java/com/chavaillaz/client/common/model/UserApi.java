package com.chavaillaz.client.common.model;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.common.ContentTypes.APPLICATION_JSON;

import java.util.concurrent.CompletableFuture;

public interface UserApi extends AutoCloseable {

    String URL_USER = "/user";

    static void stubForUserApi() {
        stubFor(get("/user").willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", APPLICATION_JSON)
                .withBody("{\"username\": \"WireMock\"}")));
    }

    /**
     * Gets the current user.
     *
     * @return A {@link CompletableFuture} with the user
     */
    CompletableFuture<User> getUser();

}
