package com.chavaillaz.client.common;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.type;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.chavaillaz.client.common.security.AnonymousAuthentication;
import com.chavaillaz.client.common.security.PasswordAuthentication;
import com.chavaillaz.client.common.security.TokenAuthentication;
import com.chavaillaz.client.common.utility.ProxyConfiguration;

@DisplayName("AbstractClient")
class AbstractClientTest {

    @Test
    @DisplayName("Starts anonymous, without proxy")
    void testDefaults() {
        TestClient client = new TestClient();

        assertThat(client.baseUrl).isEqualTo("http://localhost:8080");
        assertThat(client.authentication).isExactlyInstanceOf(AnonymousAuthentication.class);
        assertThat(client.proxy).isNull();
    }

    @Test
    @DisplayName("Uses the proxy of a URL")
    void testProxyOfUrl() {
        TestClient client = new TestClient();

        assertThat(client.withProxy("http://proxy.mycompany.com:3128")).isSameAs(client);
        assertThat(client.proxy)
                .extracting(ProxyConfiguration::getUrl)
                .isEqualTo("http://proxy.mycompany.com:3128");
    }

    @Test
    @DisplayName("Uses the proxy of a host and a port")
    void testProxyOfHost() {
        TestClient client = new TestClient().withProxy("proxy.mycompany.com", 3128);

        assertThat(client.proxy)
                .extracting(ProxyConfiguration::getUrl)
                .isEqualTo("proxy.mycompany.com:3128");
    }

    @Test
    @DisplayName("Authenticates with a token, with the password of a user, or anonymously again")
    void testAuthentications() {
        TestClient client = new TestClient();

        assertThat(client.withTokenAuthentication("Token").authentication)
                .asInstanceOf(type(TokenAuthentication.class))
                .extracting(TokenAuthentication::getToken)
                .isEqualTo("Token");
        assertThat(client.withUserAuthentication("User", "Password").authentication)
                .asInstanceOf(type(PasswordAuthentication.class))
                .extracting(PasswordAuthentication::getUsername, PasswordAuthentication::getPassword)
                .containsExactly("User", "Password");
        assertThat(client.withAnonymousAuthentication().authentication)
                .isExactlyInstanceOf(AnonymousAuthentication.class);
    }

    /**
     * A client that creates no HTTP client, the tests only looking at what it is configured with.
     */
    private static class TestClient extends AbstractClient<Object, TestClient> {

        TestClient() {
            super("http://localhost:8080");
        }

        @Override
        protected Object newHttpClient() {
            return new Object();
        }

    }

}
