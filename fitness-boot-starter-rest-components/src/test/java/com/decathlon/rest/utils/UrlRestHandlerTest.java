/* AssentSoftware (C)2023 */
package com.decathlon.rest.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class UrlRestHandlerTest {

    private UrlRestHandler urlRestHandler;

    @BeforeEach
    void init() {
        urlRestHandler = new UrlRestHandler("http", "localhost", "8080", "service");
    }

    @Test
    void buildURI() {
        String uri = urlRestHandler.buildURI("/getIdByUser");
        assertEquals("http://localhost:8080/service/getIdByUser", uri);

        uri = urlRestHandler.buildEncodedURI("/getIdByUser");
        assertEquals("http://localhost:8080/service/getIdByUser", uri);
    }

    @Test
    void buildURIWithParam() {
        Map<String, Object> valores = new HashMap<>();
        valores.put("a", "b");

        String uri = urlRestHandler.buildURI("/getIdByUser", valores, null);
        assertEquals("http://localhost:8080/service/getIdByUser?a=b", uri);
    }

    @Test
    void buildURIWithParams() {
        Map<String, Object> valores = new HashMap<>();
        valores.put("a", "b");
        valores.put("b", "c");

        String uri = urlRestHandler.buildURI("/getIdByUser", valores, null);
        assertEquals("http://localhost:8080/service/getIdByUser?a=b&b=c", uri);
    }

    @Test
    void buildURIWithPathParams() {
        Map<String, String> valores = new HashMap<>();
        valores.put("id", "200");

        String uri = urlRestHandler.buildURI("/getIdByUser/{id}", null, valores);
        assertEquals("http://localhost:8080/service/getIdByUser/200", uri);

        uri = urlRestHandler.buildURI("/getIdByUser/{id}", null, valores, true);
        assertEquals("http://localhost:8080/service/getIdByUser/200", uri);
    }

    @Test
    void buildFilterParams() {
        Map<String, Object> valores = new HashMap<>();
        valores.put("a", "b");
        valores.put("b", "&%\"c");

        String uri =
                urlRestHandler.buildURI(
                        "/getIdByUser",
                        urlRestHandler.buildFilterParams(1, 2, "name", "ASCENDING", valores));
        assertEquals(
                "http://localhost:8080/service/getIdByUser?a=b&b=&%\"c&size=2&page=0&sort=name,asc",
                uri);

        uri =
                urlRestHandler.buildEncodedURI(
                        "/getIdByUser",
                        urlRestHandler.buildFilterParams(1, 2, "name", "ASCENDING", valores));
        assertEquals(
                "http://localhost:8080/service/getIdByUser?a=b&b=%26%25%22c&size=2&page=0&sort=name,asc",
                uri);

        uri =
                urlRestHandler.buildURI(
                        "/getIdByUser",
                        urlRestHandler.buildFilterParams(1, 2, "name", "ASCENDING", null),
                        null);
        assertEquals("http://localhost:8080/service/getIdByUser?size=2&page=0&sort=name,asc", uri);

        uri =
                urlRestHandler.buildURI(
                        "/getIdByUser",
                        urlRestHandler.buildFilterParams(
                                1, 2, "name", "ASCENDING", new HashMap<>()),
                        null);
        assertEquals("http://localhost:8080/service/getIdByUser?size=2&page=0&sort=name,asc", uri);

        uri =
                urlRestHandler.buildURI(
                        "/getIdByUser",
                        urlRestHandler.buildFilterParams(1, 2, "name", null, null),
                        null);
        assertEquals("http://localhost:8080/service/getIdByUser?size=2&page=0&sort=name", uri);

        uri =
                urlRestHandler.buildURI(
                        "/getIdByUser",
                        urlRestHandler.buildFilterParams(1, 2, null, null, null),
                        null);
        assertEquals("http://localhost:8080/service/getIdByUser?size=2&page=0", uri);

        uri = urlRestHandler.buildURI("/getIdByUser", urlRestHandler.buildFilterParams(null), null);
        assertEquals("http://localhost:8080/service/getIdByUser", uri);

        uri =
                urlRestHandler.buildURI(
                        "/getIdByUser", urlRestHandler.buildFilterParams(new HashMap<>()), null);
        assertEquals("http://localhost:8080/service/getIdByUser", uri);
    }
}
