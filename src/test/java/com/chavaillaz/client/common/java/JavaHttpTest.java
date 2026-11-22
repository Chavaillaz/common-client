package com.chavaillaz.client.common.java;

import static com.chavaillaz.client.common.java.JavaHttpUtils.defaultHttpClientBuilder;

import java.net.ConnectException;

import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.DisplayName;

import com.chavaillaz.client.common.model.AbstractUserApiTest;
import com.chavaillaz.client.common.model.UserApi;
import com.chavaillaz.client.common.security.Authentication;
import com.chavaillaz.client.common.utility.ProxyConfiguration;

@DisplayName("AbstractJavaHttpClient")
class JavaHttpTest extends AbstractUserApiTest {

    @Override
    protected UserApi newUserApi(String baseUrl, Authentication authentication, @Nullable ProxyConfiguration proxy) {
        return new JavaHttpUserApi(defaultHttpClientBuilder(proxy).build(), baseUrl, authentication);
    }

    @Override
    protected Class<? extends Exception> unreachableException() {
        return ConnectException.class;
    }

}
