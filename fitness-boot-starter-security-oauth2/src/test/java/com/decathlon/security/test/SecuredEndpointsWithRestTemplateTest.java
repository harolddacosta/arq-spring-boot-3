/* Decathlon (C)2023 */
package com.decathlon.security.test;

import static org.assertj.core.api.Assertions.assertThat;

import com.decathlon.security.SecurityServicesApplication;
import com.decathlon.security.test.context.support.WithMockedUser;
import com.decathlon.security.utils.dtos.KeyValueResponseDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Locale;

@SpringBootTest(
        classes = {SecurityServicesApplication.class},
        webEnvironment = WebEnvironment.RANDOM_PORT)
class SecuredEndpointsWithRestTemplateTest {

    @LocalServerPort private int randomServerPort;

    @Autowired private RestTemplate restTemplate;

    private String baseUrl;

    @BeforeEach
    void init() {
        Locale.setDefault(new Locale("es", "ES"));

        baseUrl = "http://localhost:" + randomServerPort + "/api/v1";
    }

    @Test
    void when_is_public_access() throws Exception {
        URI uri = new URI(baseUrl + "/public/no-jwt-needed");

        ResponseEntity<KeyValueResponseDto> response =
                restTemplate.getForEntity(uri, KeyValueResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @WithMockedUser
    void when_secured_by_any_rule() throws Exception {
        URI uri = new URI(baseUrl + "/protected/by-any-rule");

        ResponseEntity<KeyValueResponseDto> response =
                restTemplate.getForEntity(uri, KeyValueResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void when_secured_by_any_rule_no_jwt() throws Exception {
        URI uri = new URI(baseUrl + "/protected/by-any-rule");

        try {

            restTemplate.getForEntity(uri, KeyValueResponseDto.class);
        } catch (HttpClientErrorException.Unauthorized e) {
            assertThat(e.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
            assertThat(e.getResponseBodyAsString())
                    .contains("Full authentication is required to access this resource");
        }
    }
}
