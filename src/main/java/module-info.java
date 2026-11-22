/**
 * Building blocks of asynchronous clients of an HTTP service, answering with a
 * {@link java.util.concurrent.CompletableFuture} through the HTTP client of the application's choice, Jackson
 * (de)serializing what they send and receive.
 * <ul>
 *     <li>{@link com.chavaillaz.client.common}: the client a library gives an application, and the base of every
 *     client of an HTTP API</li>
 *     <li>{@link com.chavaillaz.client.common.java}, {@link com.chavaillaz.client.common.apache},
 *     {@link com.chavaillaz.client.common.okhttp} and {@link com.chavaillaz.client.common.vertx}: the base of a client
 *     of an HTTP API calling it through the HTTP client of the JDK, Apache HttpClient 5, OkHttp or Vert.x</li>
 *     <li>{@link com.chavaillaz.client.common.security}: the authentications a request carries</li>
 *     <li>{@link com.chavaillaz.client.common.exception}: the exceptions a request fails with</li>
 *     <li>{@link com.chavaillaz.client.common.utility}: the proxy configuration, and helpers of the clients</li>
 * </ul>
 * Apache HttpClient 5, OkHttp and Vert.x are optional, the application bringing the one it calls the service
 * through: they are required statically, and transitively, the base of a client exposing them. None of them declares
 * a module: they are required by the automatic module names their jars give, and the warnings of javac against
 * requiring such automatic modules, transitively too, are suppressed, none of them offering another module to
 * require. Lombok generates code at compile time only.
 */
@SuppressWarnings({"requires-automatic", "requires-transitive-automatic"})
module com.chavaillaz.client.common {

    requires transitive com.fasterxml.jackson.databind;
    requires transitive java.net.http;
    requires transitive org.apache.commons.lang3;
    requires transitive org.jspecify;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires org.slf4j;

    requires static transitive io.vertx.core;
    requires static transitive io.vertx.web.client;
    requires static transitive io.vertx.web.common;
    requires static transitive okhttp3;
    requires static transitive org.apache.httpcomponents.client5.httpclient5;
    requires static transitive org.apache.httpcomponents.core5.httpcore5;
    requires static lombok;

    exports com.chavaillaz.client.common;
    exports com.chavaillaz.client.common.apache;
    exports com.chavaillaz.client.common.exception;
    exports com.chavaillaz.client.common.java;
    exports com.chavaillaz.client.common.okhttp;
    exports com.chavaillaz.client.common.security;
    exports com.chavaillaz.client.common.utility;
    exports com.chavaillaz.client.common.vertx;

}
