package org.bot;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpRequestTest {

    @Test
    void sendGetGeo() {
        HttpRequest test = new HttpRequest();
        assertEquals(test.sendGetGeo("Турнегева 4, Екатеринбург"), "56.841631 60.614938");
        assertEquals(test.sendGetGeo("Москва Моховая, 11"), "55.756856 37.612054");
        assertEquals(test.sendGetGeo("Театральная пл., 1, Москва"), "55.760069 37.618632");
        assertEquals(test.sendGetGeo("Villa 516, Jumeirah 3, Dubai"), "25.181257 55.224251");
    }

}