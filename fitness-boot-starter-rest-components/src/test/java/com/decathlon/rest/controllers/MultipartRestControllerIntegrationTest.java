/* AssentSoftware (C)2023 */
package com.decathlon.rest.controllers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.decathlon.rest.RestServicesConfiguration;
import com.decathlon.rest.utils.dtos.KeyValueResponseDto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.zalando.problem.ThrowableProblem;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@SpringBootTest(
        classes = {RestServicesConfiguration.class},
        webEnvironment = WebEnvironment.RANDOM_PORT)
class MultipartRestControllerIntegrationTest {

    @LocalServerPort private int randomServerPort;

    @Autowired private RestTemplate restTemplate;

    @Autowired private ResourceLoader resourceLoader = null;

    @Test
    void error_when_file_too_big() throws URISyntaxException, IOException {
        final String baseUrl = "http://localhost:" + randomServerPort + "/api/v1/multipart";
        URI uri = new URI(baseUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("document", getTestFile("20MBFile.txt"));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        try {
            restTemplate.postForEntity(uri, requestEntity, KeyValueResponseDto.class);

            assertFalse(true);
        } catch (ThrowableProblem e) {
            assertEquals(
                    "Bad Request: Failed to parse multipart servlet request",
                    e.getMessage().subSequence(0, 54));
        }
    }

    @Test
    void ok_when_file_is_size_allowed() throws URISyntaxException, IOException {
        final String baseUrl = "http://localhost:" + randomServerPort + "/api/v1/multipart";
        URI uri = new URI(baseUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("document", getTestFile("5MBFile.txt"));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<KeyValueResponseDto> result =
                restTemplate.postForEntity(uri, requestEntity, KeyValueResponseDto.class);

        assertThat(result.getStatusCode(), is(HttpStatus.OK));
    }

    private FileSystemResource getTestFile(String name) throws IOException {
        return new FileSystemResource(
                resourceLoader.getResource("classpath:filesToUpload/" + name).getFile());
    }
}
