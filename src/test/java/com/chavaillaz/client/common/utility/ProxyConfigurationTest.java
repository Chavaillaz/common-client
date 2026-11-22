package com.chavaillaz.client.common.utility;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ProxyConfigurationTest {

    private final String URL = "https://proxy.mycompany.com:8443";

    @Test
    void testFromUrl() {
        ProxyConfiguration proxy = ProxyConfiguration.from(URL);
        assertThat(proxy).isNotNull();
        assertThat(proxy.getUrl()).isEqualTo(URL);
        assertThat(proxy.getScheme()).isEqualTo("https");
        assertThat(proxy.getHost()).isEqualTo("proxy.mycompany.com");
        assertThat(proxy.getPort()).isEqualTo(8443);
    }

    @Test
    void testFromWrongUrl() {
        ProxyConfiguration proxy = ProxyConfiguration.from("Not an URL");
        assertThat(proxy).isNull();
    }

    @Test
    void testFromSchemeAndHostAndPort() {
        ProxyConfiguration proxy = ProxyConfiguration.from("https", "proxy.mycompany.com", 8443);
        assertThat(proxy).isNotNull();
        assertThat(proxy.getUrl()).isEqualTo(URL);
        assertThat(proxy.getScheme()).isEqualTo("https");
        assertThat(proxy.getHost()).isEqualTo("proxy.mycompany.com");
        assertThat(proxy.getPort()).isEqualTo(8443);
    }

    @Test
    void testFromHostAndPort() {
        ProxyConfiguration proxy = ProxyConfiguration.from("proxy.mycompany.com", 8443);
        assertThat(proxy).isNotNull();
        assertThat(proxy.getUrl()).isEqualTo("proxy.mycompany.com:8443");
        assertThat(proxy.getScheme()).isNull();
        assertThat(proxy.getHost()).isEqualTo("proxy.mycompany.com");
        assertThat(proxy.getPort()).isEqualTo(8443);
    }

}
