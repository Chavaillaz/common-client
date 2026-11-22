package com.chavaillaz.client.common.apache;

import static com.chavaillaz.client.common.apache.ApacheHttpUtils.defaultHttpClientBuilder;

import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.DisplayName;

import com.chavaillaz.client.common.exception.RequestException;
import com.chavaillaz.client.common.model.AbstractUserApiTest;
import com.chavaillaz.client.common.model.UserApi;
import com.chavaillaz.client.common.security.Authentication;
import com.chavaillaz.client.common.utility.ProxyConfiguration;

@DisplayName("AbstractApacheHttpClient")
class ApacheHttpTest extends AbstractUserApiTest {

    @Override
    protected UserApi newUserApi(String baseUrl, Authentication authentication, @Nullable ProxyConfiguration proxy) {
        return new ApacheHttpUserApi(defaultHttpClientBuilder(proxy).build(), baseUrl, authentication);
    }

    @Override
    protected Class<? extends Exception> unreachableException() {
        return RequestException.class;
    }

}
