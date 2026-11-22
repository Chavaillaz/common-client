package com.chavaillaz.client.common.okhttp;

import static com.chavaillaz.client.common.model.UserApi.URL_PREFERENCES;
import static com.chavaillaz.client.common.okhttp.OkHttpUtils.defaultHttpClientBuilder;
import static com.github.tomakehurst.wiremock.client.WireMock.aMultipart;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.DisplayName;

import com.chavaillaz.client.common.exception.RequestException;
import com.chavaillaz.client.common.model.AbstractUserApiTest;
import com.chavaillaz.client.common.model.UserApi;
import com.chavaillaz.client.common.security.Authentication;
import com.chavaillaz.client.common.utility.ProxyConfiguration;

@DisplayName("AbstractOkHttpClient")
class OkHttpTest extends AbstractUserApiTest {

    @Override
    protected UserApi newUserApi(String baseUrl, Authentication authentication, @Nullable ProxyConfiguration proxy) {
        return new OkHttpUserApi(defaultHttpClientBuilder(proxy).build(), baseUrl, authentication);
    }

    @Override
    protected Class<? extends Exception> unreachableException() {
        return RequestException.class;
    }

    @Override
    protected RequestPatternBuilder formRequest(String name, String value) {
        // OkHttpUtils.formData sends the entries as the parts of a multipart form
        return postRequestedFor(urlEqualTo(URL_PREFERENCES))
                .withAnyRequestBodyPart(aMultipart()
                        .withName(name)
                        .withBody(equalTo(value)));
    }

}
