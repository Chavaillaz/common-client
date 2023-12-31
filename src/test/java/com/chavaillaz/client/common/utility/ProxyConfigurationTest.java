package com.chavaillaz.client.common.utility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class ProxyConfigurationTest {

    private final String URL = "https://proxy.mycompany.com:8443";

    @Test
    void testFromUrl() {
        ProxyConfiguration proxy = ProxyConfiguration.from(URL);
        assertNotNull(proxy);
        assertEquals(URL, proxy.getUrl());
        assertEquals("https", proxy.getScheme());
        assertEquals("proxy.mycompany.com", proxy.getHost());
        assertEquals(8443, proxy.getPort());
    }

    @Test
    void testFromWrongUrl() {
        ProxyConfiguration proxy = ProxyConfiguration.from("Not an URL");
        assertNull(proxy);
    }

    @Test
    void testFromSchemeAndHostAndPort() {
        ProxyConfiguration proxy = ProxyConfiguration.from("https", "proxy.mycompany.com", 8443);
        assertNotNull(proxy);
        assertEquals(URL, proxy.getUrl());
        assertEquals("https", proxy.getScheme());
        assertEquals("proxy.mycompany.com", proxy.getHost());
        assertEquals(8443, proxy.getPort());
    }

    @Test
    void testFromHostAndPort() {
        ProxyConfiguration proxy = ProxyConfiguration.from("proxy.mycompany.com", 8443);
        assertNotNull(proxy);
        assertEquals("proxy.mycompany.com:8443", proxy.getUrl());
        assertNull(proxy.getScheme());
        assertEquals("proxy.mycompany.com", proxy.getHost());
        assertEquals(8443, proxy.getPort());
    }

}
