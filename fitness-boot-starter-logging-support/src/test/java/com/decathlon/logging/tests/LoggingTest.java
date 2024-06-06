/* AssentSoftware (C)2023 */
package com.decathlon.logging.tests;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.decathlon.logging.LoggingApplication;
import com.decathlon.logging.dto.PersonDto;
import com.decathlon.logging.filters.CorrelationMDCInjectionFilter;
import com.decathlon.logging.utils.ObjectsBuilderUtils;
import com.github.javafaker.Faker;

import jakarta.servlet.ServletException;

import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.util.UriComponentsBuilder;
import org.zalando.logbook.core.DefaultCorrelationId;

import java.io.IOException;
import java.util.Locale;

@SpringBootTest(classes = LoggingApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
class LoggingTest {

    @LocalServerPort private int localServerPort;

    @Autowired private TestRestTemplate restTemplate;

    @Spy
    private final CorrelationMDCInjectionFilter correlationMDCInjectionFilter =
            new CorrelationMDCInjectionFilter(new DefaultCorrelationId());

    private final Faker faker = new Faker(new Locale("es", "ES"));

    @Test
    void error_when_identity_document_exists_on_second_save() {
        PersonDto entityToSave = ObjectsBuilderUtils.createFullPersonDto(faker);

        HttpEntity<PersonDto> requestEntity = new HttpEntity<>(entityToSave);

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromUriString(
                                "http://localhost:" + localServerPort + "/api/v1/persons")
                        // Add query parameter
                        .queryParam("local_server_port", localServerPort);

        ResponseEntity<PersonDto> response =
                restTemplate
                        .withBasicAuth("Harold Da Costa", "secret")
                        .postForEntity(builder.toUriString(), requestEntity, PersonDto.class);

        assertNotNull(response.getBody());
    }

    @Test
    void redirectTest() throws IOException, ServletException {
        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse res = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        UsernamePasswordAuthenticationToken principal =
                new UsernamePasswordAuthenticationToken("Harold Da Costa", "secret");
        req.setUserPrincipal(principal);

        correlationMDCInjectionFilter.doFilter(req, res, chain);

        verify(correlationMDCInjectionFilter, times(1)).doFilter(req, res, chain);
        verify(correlationMDCInjectionFilter, times(1))
                .extractPrincipalInfo(req.getUserPrincipal());
    }
}
