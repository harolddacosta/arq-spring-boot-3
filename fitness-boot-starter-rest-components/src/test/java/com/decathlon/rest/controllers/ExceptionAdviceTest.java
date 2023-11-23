/* Decathlon (C)2023 */
package com.decathlon.rest.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import com.decathlon.core.exceptions.LogicException;
import com.decathlon.core.response.FieldErrorResource;
import com.decathlon.rest.RestServicesConfiguration;
import com.decathlon.rest.dtos.ValidationAnnotatedDto;
import com.decathlon.rest.utils.dtos.KeyValueResponseDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.zalando.problem.DefaultProblem;
import org.zalando.problem.Status;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

@SpringBootTest(
        classes = {RestServicesConfiguration.class},
        webEnvironment = WebEnvironment.RANDOM_PORT)
class ExceptionAdviceTest {

    @LocalServerPort private int randomServerPort;

    @Autowired private RestTemplate restTemplate;

    private String baseUrl;

    @BeforeEach
    void init() {
        Locale.setDefault(new Locale("es", "ES"));

        baseUrl = "http://localhost:" + randomServerPort + "/api/v1/exception-handling";
    }

    @Test
    void test_exception_throwing_handle_logic_exception() throws URISyntaxException {
        URI uri = new URI(baseUrl + "/logic-exception");

        LogicException exception =
                catchThrowableOfType(
                        () -> restTemplate.getForEntity(uri, KeyValueResponseDto.class),
                        LogicException.class);

        assertThat(exception.getTitle()).isEqualTo("Exception title");
        assertThat(exception.getStatus()).isEqualTo(Status.BAD_REQUEST);
        assertThat(exception.getDetail()).isEqualTo("Exception detail");
        assertThat(exception.getCode()).isEqualTo(2000);
        assertThat(exception.getUri())
                .isEqualTo(URI.create("/api/v1/exception-handling/logic-exception"));
        assertThat(exception.getTranslationKey()).isEqualTo("error.logic-exception");
        assertThat(exception.getTime()).isNotNull();
        assertThat(exception.getFieldErrors()).isEmpty();
        assertThat(exception.getTranslationKeyParameters()).isNull();
    }

    @Test
    void test_exception_throwing_handle_logic_exception_with_key_parameters()
            throws URISyntaxException {
        URI uri = new URI(baseUrl + "/logic-exception-key-parameters");

        LogicException exception =
                catchThrowableOfType(
                        () -> restTemplate.getForEntity(uri, KeyValueResponseDto.class),
                        LogicException.class);

        assertThat(exception.getTitle()).isEqualTo("Exception title");
        assertThat(exception.getStatus()).isEqualTo(Status.BAD_REQUEST);
        assertThat(exception.getDetail()).isEqualTo("Exception detail");
        assertThat(exception.getCode()).isEqualTo(2000);
        assertThat(exception.getUri())
                .isEqualTo(URI.create("/api/v1/exception-handling/logic-exception-key-parameters"));
        assertThat(exception.getTranslationKey()).isEqualTo("error.logic-exception");
        assertThat(exception.getTime()).isNotNull();
        assertThat(exception.getFieldErrors()).isEmpty();
        assertThat(exception.getTranslationKeyParameters()).isNotEmpty();
    }

    @Test
    void test_exception_throwing_handle_logic_exception_with_key_parameters_and_violations()
            throws URISyntaxException {
        URI uri = new URI(baseUrl + "/logic-exception-key-parameters-and-violations");

        LogicException exception =
                catchThrowableOfType(
                        () -> restTemplate.getForEntity(uri, KeyValueResponseDto.class),
                        LogicException.class);

        assertThat(exception.getTitle()).isEqualTo("Exception title");
        assertThat(exception.getStatus()).isEqualTo(Status.BAD_REQUEST);
        assertThat(exception.getDetail()).isEqualTo("Exception detail");
        assertThat(exception.getCode()).isEqualTo(2000);
        assertThat(exception.getUri())
                .isEqualTo(
                        URI.create(
                                "/api/v1/exception-handling/logic-exception-key-parameters-and-violations"));
        assertThat(exception.getTranslationKey()).isEqualTo("error.logic-exception");
        assertThat(exception.getTime()).isNotNull();
        assertThat(exception.getFieldErrors()).isNotEmpty();
        assertThat(exception.getFieldErrors()).doesNotHaveDuplicates();
        assertThat(exception.getFieldErrors())
                .containsOnly(
                        FieldErrorResource.builder()
                                .title("FieldError title")
                                .detail("FieldError detail")
                                .translationKey("error.field.logic-exception")
                                .build());
        assertThat(exception.getTranslationKeyParameters()).isNotEmpty();
    }

    @Test
    void test_exception_throwing_handle_logic_exception_with_fields() throws URISyntaxException {
        URI uri = new URI(baseUrl + "/logic-exception-fields");

        LogicException exception =
                catchThrowableOfType(
                        () -> restTemplate.getForEntity(uri, KeyValueResponseDto.class),
                        LogicException.class);

        assertThat(exception.getTitle()).isEqualTo("Exception title");
        assertThat(exception.getStatus()).isEqualTo(Status.BAD_REQUEST);
        assertThat(exception.getDetail()).isEqualTo("Exception detail");
        assertThat(exception.getCode()).isEqualTo(2000);
        assertThat(exception.getUri())
                .isEqualTo(URI.create("/api/v1/exception-handling/logic-exception-fields"));
        assertThat(exception.getTranslationKey()).isEqualTo("error.logic-exception");
        assertThat(exception.getTime()).isNotNull();
        assertThat(exception.getFieldErrors()).isNotEmpty();
        assertThat(exception.getFieldErrors()).doesNotHaveDuplicates();
        assertThat(exception.getFieldErrors())
                .containsOnly(
                        FieldErrorResource.builder()
                                .title("FieldError title")
                                .detail("FieldError detail")
                                .translationKey("error.field.logic-exception")
                                .build());
        assertThat(exception.getTranslationKeyParameters()).isNull();
    }

    @Test
    void test_exception_throwing_handle_logic_exception_translated() throws URISyntaxException {
        URI uri = new URI(baseUrl + "/logic-exception-translated");

        LogicException exception =
                catchThrowableOfType(
                        () -> restTemplate.getForEntity(uri, KeyValueResponseDto.class),
                        LogicException.class);

        assertThat(exception.getTitle()).isEqualTo("Exception title");
        assertThat(exception.getStatus()).isEqualTo(Status.BAD_REQUEST);
        assertThat(exception.getDetail()).isEqualTo("Excepcion traducida");
        assertThat(exception.getCode()).isEqualTo(2000);
        assertThat(exception.getUri())
                .isEqualTo(URI.create("/api/v1/exception-handling/logic-exception-translated"));
        assertThat(exception.getTranslationKey()).isEqualTo("error.logic-exception-translated");
        assertThat(exception.getTime()).isNotNull();
        assertThat(exception.getFieldErrors()).isEmpty();
        assertThat(exception.getTranslationKeyParameters()).isNull();
    }

    @Test
    void test_exception_throwing_handle_logic_exception_translated_en() throws URISyntaxException {
        URI uri = new URI(baseUrl + "/logic-exception-translated");

        Locale.setDefault(new Locale("en", "EN"));

        LogicException exception =
                catchThrowableOfType(
                        () -> restTemplate.getForEntity(uri, KeyValueResponseDto.class),
                        LogicException.class);

        assertThat(exception.getTitle()).isEqualTo("Exception title");
        assertThat(exception.getStatus()).isEqualTo(Status.BAD_REQUEST);
        assertThat(exception.getDetail()).isEqualTo("Translated exception");
        assertThat(exception.getCode()).isEqualTo(2000);
        assertThat(exception.getUri())
                .isEqualTo(URI.create("/api/v1/exception-handling/logic-exception-translated"));
        assertThat(exception.getTranslationKey()).isEqualTo("error.logic-exception-translated");
        assertThat(exception.getTime()).isNotNull();
        assertThat(exception.getFieldErrors()).isEmpty();
        assertThat(exception.getTranslationKeyParameters()).isNull();
    }

    @Test
    void test_exception_throwing_handle_logic_exception_translated_with_fields()
            throws URISyntaxException {
        URI uri = new URI(baseUrl + "/logic-exception-translated-fields");

        LogicException exception =
                catchThrowableOfType(
                        () -> restTemplate.getForEntity(uri, KeyValueResponseDto.class),
                        LogicException.class);

        assertThat(exception.getTitle()).isEqualTo("Exception title");
        assertThat(exception.getStatus()).isEqualTo(Status.BAD_REQUEST);
        assertThat(exception.getDetail()).isEqualTo("Excepcion traducida");
        assertThat(exception.getCode()).isEqualTo(2000);
        assertThat(exception.getUri())
                .isEqualTo(
                        URI.create("/api/v1/exception-handling/logic-exception-translated-fields"));
        assertThat(exception.getTranslationKey()).isEqualTo("error.logic-exception-translated");
        assertThat(exception.getTime()).isNotNull();
        assertThat(exception.getFieldErrors()).isNotEmpty();
        assertThat(exception.getFieldErrors()).doesNotHaveDuplicates();
        assertThat(exception.getFieldErrors())
                .containsOnly(
                        FieldErrorResource.builder()
                                .title("FieldError title")
                                .detail("Excepcion en campo traducida")
                                .translationKey("error.field.logic-exception-translated")
                                .build());
        assertThat(exception.getTranslationKeyParameters()).isNull();
    }

    @Test
    void test_exception_throwing_handle_logic_exception_translated_with_fields_en()
            throws URISyntaxException {
        URI uri = new URI(baseUrl + "/logic-exception-translated-fields");

        Locale.setDefault(new Locale("en", "EN"));

        LogicException exception =
                catchThrowableOfType(
                        () -> restTemplate.getForEntity(uri, KeyValueResponseDto.class),
                        LogicException.class);

        assertThat(exception.getTitle()).isEqualTo("Exception title");
        assertThat(exception.getStatus()).isEqualTo(Status.BAD_REQUEST);
        assertThat(exception.getDetail()).isEqualTo("Translated exception");
        assertThat(exception.getCode()).isEqualTo(2000);
        assertThat(exception.getUri())
                .isEqualTo(
                        URI.create("/api/v1/exception-handling/logic-exception-translated-fields"));
        assertThat(exception.getTranslationKey()).isEqualTo("error.logic-exception-translated");
        assertThat(exception.getTime()).isNotNull();
        assertThat(exception.getFieldErrors()).isNotEmpty();
        assertThat(exception.getFieldErrors()).doesNotHaveDuplicates();
        assertThat(exception.getFieldErrors())
                .containsOnly(
                        FieldErrorResource.builder()
                                .title("FieldError title")
                                .detail("Translated field exception")
                                .translationKey("error.field.logic-exception-translated")
                                .build());
        assertThat(exception.getTranslationKeyParameters()).isNull();
    }

    @Test
    void test_exception_throwing_handle_missing_param() throws URISyntaxException {
        URI uri = new URI(baseUrl + "/missing-param");

        DefaultProblem exception =
                catchThrowableOfType(
                        () -> restTemplate.getForEntity(uri, KeyValueResponseDto.class),
                        DefaultProblem.class);

        assertThat(exception.getTitle()).isEqualTo("Bad Request");
        assertThat(exception.getStatus()).isEqualTo(Status.BAD_REQUEST);
        assertThat(exception.getDetail())
                .isEqualTo(
                        "Required request parameter 'key' for method parameter type String is not present");
    }

    @Test
    void test_exception_throwing_file_excedded() throws URISyntaxException {
        URI uri = new URI(baseUrl + "/exceed-file-size");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        byte[] b = new byte[20];
        new Random().nextBytes(b);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", b);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        DefaultProblem exception =
                catchThrowableOfType(
                        () ->
                                restTemplate.exchange(
                                        uri, HttpMethod.POST, requestEntity, String.class),
                        DefaultProblem.class);

        assertThat(exception.getTitle()).isEqualTo("Bad Request");
        assertThat(exception.getStatus()).isEqualTo(Status.BAD_REQUEST);
        assertThat(exception.getDetail()).isEqualTo("Required part 'file' is not present.");
    }

    @SuppressWarnings("unchecked")
    @Test
    void test_exception_throwing_handle_validate_dto() throws URISyntaxException {
        URI uri = new URI(baseUrl + "/validate-dto");

        ValidationAnnotatedDto body = new ValidationAnnotatedDto();

        DefaultProblem exception =
                catchThrowableOfType( // NOSONAR
                        () ->
                                restTemplate.postForEntity(
                                        uri, new HttpEntity<>(body), KeyValueResponseDto.class),
                        DefaultProblem.class);

        assertThat(exception.getTitle()).isEqualTo("Constraint Violation");
        assertThat(exception.getStatus()).isEqualTo(Status.BAD_REQUEST);
        assertThat(exception.getDetail()).isNull();
        assertThat(exception.getParameters()).isNotEmpty();
        assertThat(exception.getParameters()).containsKey("violations");
        assertThat(
                        (ArrayList<LinkedHashMap<String, String>>)
                                exception.getParameters().get("violations"))
                .containsExactly(
                        new LinkedHashMap<>(
                                Map.of("field", "name", "message", "no debe ser nulo")));
    }

    @SuppressWarnings("unchecked")
    @Test
    void test_exception_throwing_handle_validate_dto_size() throws URISyntaxException {
        URI uri = new URI(baseUrl + "/validate-dto");

        ValidationAnnotatedDto body = new ValidationAnnotatedDto();
        body.setName("123");

        DefaultProblem exception =
                catchThrowableOfType( // NOSONAR
                        () ->
                                restTemplate.postForEntity(
                                        uri, new HttpEntity<>(body), KeyValueResponseDto.class),
                        DefaultProblem.class);

        assertThat(exception.getTitle()).isEqualTo("Constraint Violation");
        assertThat(exception.getStatus()).isEqualTo(Status.BAD_REQUEST);
        assertThat(exception.getDetail()).isNull();
        assertThat(exception.getParameters()).isNotEmpty();
        assertThat(exception.getParameters()).containsKey("violations");
        assertThat(
                        (ArrayList<LinkedHashMap<String, String>>)
                                exception.getParameters().get("violations"))
                .containsExactly(
                        new LinkedHashMap<>(
                                Map.of(
                                        "field",
                                        "name",
                                        "message",
                                        "el tama\u00f1o debe estar entre 5 y 2147483647")));
    }

    @SuppressWarnings("unchecked")
    @Test
    void test_exception_throwing_handle_validate_dto_size_en() throws URISyntaxException {
        URI uri = new URI(baseUrl + "/validate-dto");

        ValidationAnnotatedDto body = new ValidationAnnotatedDto();
        body.setName("123");

        Locale.setDefault(new Locale("en", "EN"));

        DefaultProblem exception =
                catchThrowableOfType( // NOSONAR
                        () ->
                                restTemplate.postForEntity(
                                        uri, new HttpEntity<>(body), KeyValueResponseDto.class),
                        DefaultProblem.class);

        assertThat(exception.getTitle()).isEqualTo("Constraint Violation");
        assertThat(exception.getStatus()).isEqualTo(Status.BAD_REQUEST);
        assertThat(exception.getDetail()).isNull();
        assertThat(exception.getParameters()).isNotEmpty();
        assertThat(exception.getParameters()).containsKey("violations");
        assertThat(
                        (ArrayList<LinkedHashMap<String, String>>)
                                exception.getParameters().get("violations"))
                .containsExactly(
                        new LinkedHashMap<>(
                                Map.of(
                                        "field",
                                        "name",
                                        "message",
                                        "size must be between 5 and 2147483647")));
    }

    @SuppressWarnings("unchecked")
    @Test
    void test_exception_throwing_handle_validate_dto_no_annotation() throws URISyntaxException {
        URI uri = new URI(baseUrl + "/validate-dto-no-annotation");

        ValidationAnnotatedDto body = new ValidationAnnotatedDto();

        DefaultProblem exception =
                catchThrowableOfType( // NOSONAR
                        () ->
                                restTemplate.postForEntity(
                                        uri, new HttpEntity<>(body), KeyValueResponseDto.class),
                        DefaultProblem.class);

        assertThat(exception.getTitle()).isEqualTo("Constraint Violation");
        assertThat(exception.getStatus()).isEqualTo(Status.BAD_REQUEST);
        assertThat(exception.getDetail()).isNull();
        assertThat(exception.getParameters()).isNotEmpty();
        assertThat(exception.getParameters()).containsKey("violations");
        assertThat(
                        (ArrayList<LinkedHashMap<String, String>>)
                                exception.getParameters().get("violations"))
                .containsExactly(
                        new LinkedHashMap<>(
                                Map.of("field", "name", "message", "no debe ser nulo")));
    }

    @SuppressWarnings("unchecked")
    @Test
    void test_exception_throwing_handle_validate_dto_size_no_annotation()
            throws URISyntaxException {
        URI uri = new URI(baseUrl + "/validate-dto-no-annotation");

        ValidationAnnotatedDto body = new ValidationAnnotatedDto();
        body.setName("123");

        DefaultProblem exception =
                catchThrowableOfType( // NOSONAR
                        () ->
                                restTemplate.postForEntity(
                                        uri, new HttpEntity<>(body), KeyValueResponseDto.class),
                        DefaultProblem.class);

        assertThat(exception.getTitle()).isEqualTo("Constraint Violation");
        assertThat(exception.getStatus()).isEqualTo(Status.BAD_REQUEST);
        assertThat(exception.getDetail()).isNull();
        assertThat(exception.getParameters()).isNotEmpty();
        assertThat(exception.getParameters()).containsKey("violations");
        assertThat(
                        (ArrayList<LinkedHashMap<String, String>>)
                                exception.getParameters().get("violations"))
                .containsExactly(
                        new LinkedHashMap<>(
                                Map.of(
                                        "field",
                                        "name",
                                        "message",
                                        "el tama\u00f1o debe estar entre 5 y 2147483647")));
    }

    @Test
    void test_exception_throwing_not_service_available() throws URISyntaxException {
        URI uri = new URI(baseUrl + "/single-status-error-response");

        DefaultProblem exception =
                catchThrowableOfType(
                        () -> restTemplate.getForEntity(uri, String.class), DefaultProblem.class);

        assertThat(exception.getTitle()).isEqualTo("Service Unavailable");
        assertThat(exception.getStatus()).isEqualTo(Status.SERVICE_UNAVAILABLE);
        assertThat(exception.getDetail()).isEqualTo("Database not reachable");
    }

    @Test
    void test_exception_throwing_not_service_available_bigger_response() throws URISyntaxException {
        URI uri = new URI(baseUrl + "/single-status-error-bigger-response");

        HttpServerErrorException exception =
                catchThrowableOfType(
                        () -> restTemplate.getForEntity(uri, String.class),
                        HttpServerErrorException.class);

        assertThat(exception.getMessage()).contains("503 Service Unavailable");
    }

    @Test
    void test_exception_throwing_url_not_found() throws URISyntaxException {
        URI uri = new URI(baseUrl + "/url-not-found");

        DefaultProblem exception =
                catchThrowableOfType(
                        () -> restTemplate.getForEntity(uri, String.class), DefaultProblem.class);

        assertThat(exception.getTitle()).isEqualTo("Not Found");
        assertThat(exception.getStatus()).isEqualTo(Status.NOT_FOUND);
        assertThat(exception.getDetail())
                .isEqualTo("No endpoint GET /api/v1/exception-handling/url-not-found.");
    }

    @Test
    void test_exception_throwing_npe() throws URISyntaxException {
        URI uri = new URI(baseUrl + "/npe");

        DefaultProblem exception =
                catchThrowableOfType(
                        () -> restTemplate.getForEntity(uri, String.class), DefaultProblem.class);

        assertThat(exception.getTitle()).isEqualTo("Internal Server Error");
        assertThat(exception.getStatus()).isEqualTo(Status.INTERNAL_SERVER_ERROR);
        assertThat(exception.getDetail()).isEqualTo("NPE registered");
    }
}
