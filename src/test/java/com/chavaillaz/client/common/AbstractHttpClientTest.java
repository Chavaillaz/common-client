package com.chavaillaz.client.common;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.chavaillaz.client.common.exception.DeserializationException;
import com.chavaillaz.client.common.exception.ResponseException;
import com.chavaillaz.client.common.exception.SerializationException;
import com.chavaillaz.client.common.model.User;
import com.chavaillaz.client.common.security.AnonymousAuthentication;

@DisplayName("AbstractHttpClient")
class AbstractHttpClientTest {

    private final AbstractHttpClient client = new AbstractHttpClient("http://localhost:8080", new AnonymousAuthentication()) {
        // Nothing more than what every client has
    };

    @Test
    @DisplayName("Prefixes a path with the base URL, replacing its placeholders with the parameters")
    void testUrlOfPath() {
        assertThat(client.url("/users/{0}/groups/{1}", "chuck", 7))
                .hasToString("http://localhost:8080/users/chuck/groups/7");
    }

    @Test
    @DisplayName("Keeps a URL with a scheme as it is, but for its placeholders")
    void testUrlWithScheme() {
        assertThat(client.url("https://other.host/users/{0}", "chuck"))
                .hasToString("https://other.host/users/chuck");
    }

    @Test
    @SuppressWarnings("unchecked") // The class of a collection can only be raw
    @DisplayName("Deserializes a collection of the types given")
    void testDeserializeCollection() {
        Collection<User> users = client.deserializeCollection(
                "[{\"username\": \"First\"}, {\"username\": \"Second\"}]", List.class, User.class);

        assertThat(users)
                .isInstanceOf(List.class)
                .extracting(User::getUsername)
                .containsExactly("First", "Second");
    }

    @Test
    @SuppressWarnings("unchecked") // The class of a collection can only be raw
    @DisplayName("Fails to deserialize a collection from another content, naming the type of its elements")
    void testDeserializeCollectionFailure() {
        assertThatExceptionOfType(DeserializationException.class)
                .isThrownBy(() -> client.deserializeCollection("{\"username\": \"First\"}", List.class, User.class))
                .withMessageContaining("User");
    }

    @Test
    @SuppressWarnings("unchecked") // The class of a map can only be raw
    @DisplayName("Deserializes a map of the types given")
    void testDeserializeMap() {
        Map<String, User> users = client.deserializeMap(
                "{\"first\": {\"username\": \"First\"}}", HashMap.class, String.class, User.class);

        assertThat(users).containsOnlyKeys("first");
        assertThat(users.get("first"))
                .extracting(User::getUsername)
                .isEqualTo("First");
    }

    @Test
    @SuppressWarnings("unchecked") // The class of a map can only be raw
    @DisplayName("Fails to deserialize a map from another content, naming the type of its values")
    void testDeserializeMapFailure() {
        assertThatExceptionOfType(DeserializationException.class)
                .isThrownBy(() -> client.deserializeMap("[]", HashMap.class, String.class, User.class))
                .withMessageContaining("User");
    }

    @Test
    @DisplayName("Deserializes an object, whatever the case of its properties")
    void testDeserialize() {
        assertThat(client.deserialize("{\"USERNAME\": \"Chuck\"}", User.class))
                .extracting(User::getUsername)
                .isEqualTo("Chuck");
    }

    @Test
    @DisplayName("Deserializes nothing from a blank content, nor as Void")
    void testDeserializeNothing() {
        assertThat(client.deserialize(null, User.class)).isNull();
        assertThat(client.deserialize(" ", User.class)).isNull();
        assertThat(client.deserialize("{}", Void.class)).isNull();
    }

    @Test
    @DisplayName("Fails to deserialize an invalid content, naming it")
    void testDeserializeFailure() {
        assertThatExceptionOfType(DeserializationException.class)
                .isThrownBy(() -> client.deserialize("{invalid", User.class))
                .withMessageContaining("{invalid");
    }

    @Test
    @DisplayName("Serializes an object as JSON, leaving its null properties out")
    void testSerialize() {
        User user = new User();
        assertThat(client.serialize(user)).isEqualTo("{}");

        user.setUsername("Chuck");
        assertThat(client.serialize(user)).isEqualTo("{\"username\":\"Chuck\"}");
    }

    @Test
    @DisplayName("Fails to serialize an object Jackson has no serializer for, naming its class")
    void testSerializeFailure() {
        assertThatExceptionOfType(SerializationException.class)
                .isThrownBy(() -> client.serialize(new Object()))
                .withMessageContaining("Object");
    }

    @Test
    @DisplayName("Creates the exception of an error response, naming the request when given")
    void testResponseException() {
        assertThat(client.responseException(404, "Not Found"))
                .hasMessage("Service responded with 404: Not Found")
                .extracting(ResponseException::getStatusCode, ResponseException::getBody)
                .containsExactly(404, "Not Found");
        assertThat(client.responseException("GET", "http://localhost:8080/user", 404, "Not Found"))
                .hasMessage("Service responded with 404: Not Found for GET http://localhost:8080/user");
    }

}
