package com.chavaillaz.client.common.model;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.common.ContentTypes.APPLICATION_JSON;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * The API of users each client of the tests implements, calling the service WireMock stands for.
 */
public interface UserApi extends AutoCloseable {

    String URL_USER = "/user";
    String URL_AVATAR = "/user/avatar";
    String URL_PREFERENCES = "/user/preferences";

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

    /**
     * Gets the avatar of the current user.
     *
     * @return A {@link CompletableFuture} with the content of the avatar
     */
    CompletableFuture<InputStream> getAvatar();

    /**
     * Updates the current user.
     *
     * @param user The user to send
     * @return A {@link CompletableFuture} with the user updated
     */
    CompletableFuture<User> updateUser(User user);

    /**
     * Deletes the current user.
     *
     * @return A {@link CompletableFuture} completed once the user is deleted
     */
    CompletableFuture<Void> deleteUser();

    /**
     * Uploads the avatar of the current user.
     *
     * @param file The file of the avatar
     * @return A {@link CompletableFuture} completed once the avatar is uploaded
     * @throws IOException If the file cannot be read
     */
    CompletableFuture<Void> uploadAvatar(File file) throws IOException;

    /**
     * Updates the preferences of the current user, sent as a form.
     *
     * @param preferences The preferences to send
     * @return A {@link CompletableFuture} completed once the preferences are updated
     */
    CompletableFuture<Void> updatePreferences(Map<Object, Object> preferences);

    @Override
    void close() throws IOException;

}
