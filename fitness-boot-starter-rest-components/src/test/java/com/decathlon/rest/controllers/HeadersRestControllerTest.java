/* Decathlon (C)2023 */
package com.decathlon.rest.controllers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.decathlon.rest.RestServicesConfiguration;
import com.decathlon.rest.utils.dtos.KeyValueResponseDto;

import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;

@SpringBootTest(
        classes = {RestServicesConfiguration.class},
        webEnvironment = WebEnvironment.RANDOM_PORT)
@Slf4j
class HeadersRestControllerTest {

    @LocalServerPort private int randomServerPort;

    @Autowired private RestTemplate restTemplate;

    @Test
    void get_locale_header_default() throws URISyntaxException {
        final String baseUrl = "http://localhost:" + randomServerPort + "/api/v1/headers-test";
        URI uri = new URI(baseUrl);

        Locale.setDefault(new Locale("es", "ES"));

        ResponseEntity<KeyValueResponseDto> result =
                restTemplate.getForEntity(uri, KeyValueResponseDto.class);

        assertThat(result.getStatusCode(), is(HttpStatus.OK));
        assertThat(result.getBody().getValue(), is("Mensaje traducido"));
        assertThat(result.getHeaders().containsKey("Vary"), is(true));
        assertThat(
                result.getHeaders().get("Vary").contains("Access-Control-Request-Method"),
                is(true));
        assertThat(
                result.getHeaders().get("Vary").contains("Access-Control-Request-Headers"),
                is(true));

        log.debug("Valor:{}", result.getHeaders());
    }

    @Test
    void get_locale_header_en() throws URISyntaxException {
        final String baseUrl = "http://localhost:" + randomServerPort + "/api/v1/headers-test";
        URI uri = new URI(baseUrl);

        Locale.setDefault(new Locale("en", "EN"));

        ResponseEntity<KeyValueResponseDto> result =
                restTemplate.getForEntity(uri, KeyValueResponseDto.class);

        assertThat(result.getStatusCode(), is(HttpStatus.OK));
        assertThat(result.getBody().getValue(), is("Message translated"));
        assertThat(result.getHeaders().containsKey("Vary"), is(true));
        assertThat(
                result.getHeaders().get("Vary").contains("Access-Control-Request-Method"),
                is(true));
        assertThat(
                result.getHeaders().get("Vary").contains("Access-Control-Request-Headers"),
                is(true));

        log.debug("Valor:{}", result.getHeaders().get("Vary"));
    }
}
