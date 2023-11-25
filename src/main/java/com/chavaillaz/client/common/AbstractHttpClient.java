package com.chavaillaz.client.common;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.fasterxml.jackson.databind.MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.net.URI;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Map;

import com.chavaillaz.client.common.exception.DeserializationException;
import com.chavaillaz.client.common.exception.ResponseException;
import com.chavaillaz.client.common.exception.SerializationException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Abstract class implementing common parts for all clients.
 *
 * @param <A> The authentication type
 */
@Slf4j
@Getter
public abstract class AbstractHttpClient<A extends Authentication> {

    public static final String HEADER_COOKIE = "Cookie";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HEADER_CONTENT_JSON = "application/json";
    public static final String HEADER_CONTENT_XML = "application/xml";
    public static final String HEADER_CONTENT_FORM = "application/x-www-form-urlencoded";

    protected final A authentication;
    protected final String baseUrl;

    protected ObjectMapper objectMapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .serializationInclusion(NON_NULL)
            .enable(ACCEPT_CASE_INSENSITIVE_PROPERTIES)
            .disable(WRITE_DATES_AS_TIMESTAMPS)
            .build();

    /**
     * Creates a new abstract client.
     *
     * @param baseUrl        The base URL of endpoints
     * @param authentication The authentication information
     */
    protected AbstractHttpClient(String baseUrl, A authentication) {
        this.baseUrl = baseUrl;
        this.authentication = authentication;
    }

    /**
     * Creates a URL and replaces the parameters in it with the given method parameters.
     * Note that by giving a full URL (with scheme) it will not add the base URL to it.
     *
     * @param url        The URL with possible parameters in it (using braces like {0}, {1}, ...)
     * @param parameters The parameters value to replace in the URL (in the right order)
     * @return The final URL incorporating parameters values
     */
    protected URI url(String url, Object... parameters) {
        return URI.create((url.startsWith("http") ? EMPTY : this.baseUrl) + MessageFormat.format(url, parameters));
    }

    /**
     * Deserializes a JSON content into a {@link Collection}.
     *
     * @param content        The content to deserialize
     * @param collectionType The collection class type
     * @param objectType     The collection object class type
     * @param <C>            The collection type
     * @param <O>            The collection object type
     * @return The collection object instance of the given types
     */
    public <C extends Collection<O>, O> Collection<O> deserializeCollection(String content, Class<C> collectionType, Class<O> objectType) {
        try {
            return objectMapper.readValue(content, objectMapper.getTypeFactory()
                    .constructCollectionType(collectionType, objectType));
        } catch (Exception e) {
            throw new DeserializationException(content, objectType, e);
        }
    }

    /**
     * Deserializes a JSON content into a {@link Map}.
     *
     * @param content   The content to deserialize
     * @param mapClass  The map class type
     * @param keyType   The map key class type
     * @param valueType The map value class type
     * @param <M>       The map type
     * @param <K>       The map key type
     * @param <V>       The map value type
     * @return The map object instance of the given types
     */
    public <M extends Map<K, V>, K, V> Map<K, V> deserializeMap(String content, Class<M> mapClass, Class<K> keyType, Class<V> valueType) {
        try {
            return objectMapper.readValue(content, objectMapper.getTypeFactory()
                    .constructMapType(mapClass, keyType, valueType));
        } catch (Exception e) {
            throw new DeserializationException(content, valueType, e);
        }
    }

    /**
     * Deserializes a JSON content to the given type.
     *
     * @param content The object to deserialize
     * @param type    The object class type
     * @param <T>     The object type
     * @return The object instance of the given type
     */
    public <T> T deserialize(String content, Class<T> type) {
        return deserialize(content, objectMapper.constructType(type));
    }

    /**
     * Deserializes a JSON content to the given type.
     *
     * @param content The object to deserialize
     * @param type    The object class type
     * @param <T>     The object type
     * @return The object instance of the given type
     */
    public <T> T deserialize(String content, JavaType type) {
        if (type.getRawClass() == Void.class || isBlank(content)) {
            return null;
        }

        try {
            log.trace("Response to deserialize: {}", content);
            return objectMapper.readValue(content, type);
        } catch (Exception e) {
            throw new DeserializationException(content, type, e);
        }
    }

    /**
     * Serializes an object to JSON.
     *
     * @param content The object to serialize
     * @return The corresponding JSON value
     */
    public String serialize(Object content) {
        try {
            String json = objectMapper.writeValueAsString(content);
            log.trace("Request serialized: {}", json);
            return json;
        } catch (Exception e) {
            throw new SerializationException(content, e);
        }
    }

    /**
     * Creates a new response exception, called in case the request didn't return a success code.
     * Override this method to parse the body returned in order to obtain an understandable error message.
     *
     * @param code The status code returned
     * @param body The content body
     * @return The response exception
     */
    public ResponseException responseException(int code, String body) {
        return new ResponseException(code, body);
    }

}
