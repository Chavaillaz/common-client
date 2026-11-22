/**
 * The base of a client of an HTTP API calling it through OkHttp,
 * {@link com.chavaillaz.client.common.okhttp.AbstractOkHttpClient}, and the utilities building that client and the
 * bodies of its requests.
 * <p>
 * OkHttp is an optional dependency of the library, which an application brings to use this package.
 * <p>
 * The package is {@link org.jspecify.annotations.NullMarked}: every type is non-null unless explicitly annotated
 * {@link org.jspecify.annotations.Nullable}.
 */
@NullMarked
package com.chavaillaz.client.common.okhttp;

import org.jspecify.annotations.NullMarked;
