/**
 * The client a library gives an application, and the base of every client of an HTTP API.
 * <p>
 * {@link com.chavaillaz.client.common.Client} sets the proxy and the authentication of a client, which
 * {@link com.chavaillaz.client.common.AbstractClient} keeps for the HTTP client it creates.
 * {@link com.chavaillaz.client.common.AbstractHttpClient}, the base of a client of an HTTP API, builds the URLs of its
 * requests and (de)serializes their content with Jackson.
 * <p>
 * The package is {@link org.jspecify.annotations.NullMarked}: every type is non-null unless explicitly annotated
 * {@link org.jspecify.annotations.Nullable}.
 */
@NullMarked
package com.chavaillaz.client.common;

import org.jspecify.annotations.NullMarked;
