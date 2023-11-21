/* Decathlon (C)2023 */
package com.decathlon.rest.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.decathlon.core.exceptions.LogicException;
import com.decathlon.core.response.FieldErrorResource;
import com.decathlon.rest.RestServicesConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.zalando.problem.Problem;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {RestServicesConfiguration.class})
class ExceptionUtilsTest {

    @Autowired private ObjectMapper mappingJackson2HttpMessageConverter;

    @Test
    void test_problem_json_mapping() throws IOException {
        for (int fileIndex = 1; fileIndex <= 7; fileIndex++) {
            String jsonErrorResponse =
                    new String(
                            getClass()
                                    .getClassLoader()
                                    .getResourceAsStream(
                                            "json/error_response_" + fileIndex + ".json")
                                    .readAllBytes());

            HttpClientErrorException httpClientErrorException =
                    HttpClientErrorException.create(
                            HttpStatus.BAD_REQUEST,
                            "Bad Request",
                            null,
                            jsonErrorResponse.getBytes(),
                            StandardCharsets.UTF_8);

            Optional<Problem> errorResponse =
                    ExceptionUtils.convertFromHttpClientErrorException(
                            httpClientErrorException,
                            mappingJackson2HttpMessageConverter,
                            Problem.class);

            assertTrue(errorResponse.isPresent());

            if (fileIndex == 7) {
                assertEquals("Exception title", errorResponse.get().getTitle());
                assertEquals(400, errorResponse.get().getStatus().getStatusCode());
                assertEquals("Excepcion traducida", errorResponse.get().getDetail());
                assertEquals(2000, errorResponse.get().getParameters().get("front_end_code"));

                String fieldsErrorsString =
                        mappingJackson2HttpMessageConverter.writeValueAsString(
                                errorResponse.get().getParameters().get("violations"));

                List<FieldErrorResource> fieldErrorsResource =
                        Arrays.asList(
                                mappingJackson2HttpMessageConverter.readValue(
                                        fieldsErrorsString, FieldErrorResource[].class));

                assertEquals("FieldError title", fieldErrorsResource.get(0).getTitle());
                assertEquals(
                        "Excepcion en campo traducida", fieldErrorsResource.get(0).getDetail());
                assertEquals(
                        "error.field.logic-exception-translated",
                        fieldErrorsResource.get(0).getTranslationKey());
            }
        }
    }

    @Test
    void test_problem_json_mapping_bytes() throws IOException {
        for (int fileIndex = 1; fileIndex <= 7; fileIndex++) {
            String jsonErrorResponse =
                    new String(
                            getClass()
                                    .getClassLoader()
                                    .getResourceAsStream(
                                            "json/error_response_" + fileIndex + ".json")
                                    .readAllBytes());

            HttpClientErrorException httpClientErrorException =
                    HttpClientErrorException.create(
                            HttpStatus.BAD_REQUEST,
                            "Bad Request",
                            null,
                            jsonErrorResponse.getBytes(),
                            StandardCharsets.UTF_8);

            Optional<Problem> errorResponse =
                    ExceptionUtils.convertFromHttpClientErrorException(
                            httpClientErrorException.getResponseBodyAsByteArray(),
                            mappingJackson2HttpMessageConverter,
                            Problem.class);

            assertTrue(errorResponse.isPresent());

            if (fileIndex == 7) {
                assertEquals("Exception title", errorResponse.get().getTitle());
                assertEquals(400, errorResponse.get().getStatus().getStatusCode());
                assertEquals("Excepcion traducida", errorResponse.get().getDetail());
                assertEquals(2000, errorResponse.get().getParameters().get("front_end_code"));

                String fieldsErrorsString =
                        mappingJackson2HttpMessageConverter.writeValueAsString(
                                errorResponse.get().getParameters().get("violations"));

                List<FieldErrorResource> fieldErrorsResource =
                        Arrays.asList(
                                mappingJackson2HttpMessageConverter.readValue(
                                        fieldsErrorsString, FieldErrorResource[].class));

                assertEquals("FieldError title", fieldErrorsResource.get(0).getTitle());
                assertEquals(
                        "Excepcion en campo traducida", fieldErrorsResource.get(0).getDetail());
                assertEquals(
                        "error.field.logic-exception-translated",
                        fieldErrorsResource.get(0).getTranslationKey());
            }
        }
    }

    @Test
    void test_problem_json_mapping_bytes_error() throws IOException {
        String jsonErrorResponse =
                new String(
                        getClass()
                                .getClassLoader()
                                .getResourceAsStream("json/error_response_7.json")
                                .readAllBytes());

        HttpClientErrorException httpClientErrorException =
                HttpClientErrorException.create(
                        HttpStatus.BAD_REQUEST,
                        "Bad Request",
                        null,
                        jsonErrorResponse.getBytes(),
                        StandardCharsets.UTF_8);

        Optional<LogicException> errorResponse =
                ExceptionUtils.convertFromHttpClientErrorException(
                        httpClientErrorException.getResponseBodyAsByteArray(),
                        mappingJackson2HttpMessageConverter,
                        LogicException.class);

        assertFalse(errorResponse.isPresent());
    }

    @Test
    void test_problem_json_mapping_error() throws IOException {
        String jsonErrorResponse =
                new String(
                        getClass()
                                .getClassLoader()
                                .getResourceAsStream("json/error_response_7.json")
                                .readAllBytes());

        HttpClientErrorException httpClientErrorException =
                HttpClientErrorException.create(
                        HttpStatus.BAD_REQUEST,
                        "Bad Request",
                        null,
                        jsonErrorResponse.getBytes(),
                        StandardCharsets.UTF_8);

        Optional<NullPointerException> errorResponse =
                ExceptionUtils.convertFromHttpClientErrorException(
                        httpClientErrorException,
                        mappingJackson2HttpMessageConverter,
                        NullPointerException.class);

        assertTrue(errorResponse.isPresent());
    }
}
