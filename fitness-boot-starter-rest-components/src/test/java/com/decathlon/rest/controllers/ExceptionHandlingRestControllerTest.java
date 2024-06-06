/* AssentSoftware (C)2023 */
package com.decathlon.rest.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.decathlon.rest.config.CustomWebMvcConfiguration;
import com.decathlon.rest.config.JsonConfiguration;
import com.decathlon.rest.config.LocaleConfiguration;
import com.decathlon.rest.context.i18n.Translator;
import com.decathlon.rest.context.properties.RestConfigParameters;
import com.decathlon.rest.dtos.ValidationAnnotatedDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Locale;

@WebMvcTest(controllers = {ExceptionHandlingRestController.class})
@Import({
    RestConfigParameters.class,
    Translator.class,
    LocaleConfiguration.class,
    JsonConfiguration.class,
    CustomWebMvcConfiguration.class
})
@TestPropertySource(locations = {"classpath:rest.properties", "classpath:application.properties"})
class ExceptionHandlingRestControllerTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private ObjectMapper objectMapper;

    private String baseUrl;

    @BeforeEach
    void init() {
        Locale.setDefault(new Locale("es", "ES"));

        baseUrl = "/api/v1/exception-handling";
    }

    @Test
    void test_exception_throwing_handle_logic_exception() throws Exception {
        mockMvc.perform(get(baseUrl + "/logic-exception"))
                .andExpect(status().is4xxClientError())
                .andExpect(
                        header().string("Content-Type", "application/problem+json;charset=UTF-8"))
                .andDo(print())
                .andExpect(jsonPath("$.title", is("Exception title")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.detail", is("Exception detail")))
                .andExpect(jsonPath("$.front_end_code", is(2000)))
                .andExpect(jsonPath("$.url", is(baseUrl + "/logic-exception")))
                .andExpect(jsonPath("$.key", is("error.logic-exception")))
                .andExpect(jsonPath("$.key_parameters").doesNotExist())
                .andExpect(jsonPath("$.time").exists());
    }

    @Test
    void test_exception_throwing_handle_logic_exception_with_key_parameters() throws Exception {
        mockMvc.perform(get(baseUrl + "/logic-exception-key-parameters"))
                .andExpect(status().is4xxClientError())
                .andExpect(
                        header().string("Content-Type", "application/problem+json;charset=UTF-8"))
                .andDo(print())
                .andExpect(jsonPath("$.title", is("Exception title")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.detail", is("Exception detail")))
                .andExpect(jsonPath("$.front_end_code", is(2000)))
                .andExpect(jsonPath("$.url", is(baseUrl + "/logic-exception-key-parameters")))
                .andExpect(jsonPath("$.key", is("error.logic-exception")))
                .andExpect(jsonPath("$.key_parameters").exists())
                .andExpect(jsonPath("$.time").exists());
    }

    @Test
    void test_exception_throwing_handle_logic_exception_with_fields() throws Exception {
        mockMvc.perform(get(baseUrl + "/logic-exception-fields"))
                .andExpect(status().is4xxClientError())
                .andExpect(
                        header().string("Content-Type", "application/problem+json;charset=UTF-8"))
                .andDo(print())
                .andExpect(jsonPath("$.title", is("Exception title")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.detail", is("Exception detail")))
                .andExpect(jsonPath("$.front_end_code", is(2000)))
                .andExpect(jsonPath("$.url", is(baseUrl + "/logic-exception-fields")))
                .andExpect(jsonPath("$.key", is("error.logic-exception")))
                .andExpect(jsonPath("$.key_parameters").doesNotExist())
                .andExpect(jsonPath("$.time").exists())
                .andExpect(jsonPath("$.violations").isArray())
                .andExpect(jsonPath("$.violations[0].title", is("FieldError title")))
                .andExpect(jsonPath("$.violations[0].detail", is("FieldError detail")))
                .andExpect(
                        jsonPath(
                                "$.violations[0].translation_key",
                                is("error.field.logic-exception")));
    }

    @Test
    void test_exception_throwing_handle_logic_exception_translated() throws Exception {
        mockMvc.perform(get(baseUrl + "/logic-exception-translated"))
                .andExpect(status().is4xxClientError())
                .andExpect(
                        header().string("Content-Type", "application/problem+json;charset=UTF-8"))
                .andDo(print())
                .andExpect(jsonPath("$.title", is("Exception title")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.detail", is("Excepcion traducida")))
                .andExpect(jsonPath("$.front_end_code", is(2000)))
                .andExpect(jsonPath("$.url", is(baseUrl + "/logic-exception-translated")))
                .andExpect(jsonPath("$.key", is("error.logic-exception-translated")))
                .andExpect(jsonPath("$.key_parameters").doesNotExist())
                .andExpect(jsonPath("$.time").exists());
    }

    @Test
    void test_exception_throwing_handle_logic_exception_translated_en() throws Exception {
        mockMvc.perform(
                        get(baseUrl + "/logic-exception-translated")
                                .header("Accept-Language", "en"))
                .andExpect(status().is4xxClientError())
                .andExpect(
                        header().string("Content-Type", "application/problem+json;charset=UTF-8"))
                .andDo(print())
                .andExpect(jsonPath("$.title", is("Exception title")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.detail", is("Translated exception")))
                .andExpect(jsonPath("$.front_end_code", is(2000)))
                .andExpect(jsonPath("$.url", is(baseUrl + "/logic-exception-translated")))
                .andExpect(jsonPath("$.key", is("error.logic-exception-translated")))
                .andExpect(jsonPath("$.key_parameters").doesNotExist())
                .andExpect(jsonPath("$.time").exists());
    }

    @Test
    void test_exception_throwing_handle_logic_exception_translated_with_fields() throws Exception {
        mockMvc.perform(get(baseUrl + "/logic-exception-translated-fields"))
                .andExpect(status().is4xxClientError())
                .andExpect(
                        header().string("Content-Type", "application/problem+json;charset=UTF-8"))
                .andDo(print())
                .andExpect(jsonPath("$.title", is("Exception title")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.detail", is("Excepcion traducida")))
                .andExpect(jsonPath("$.front_end_code", is(2000)))
                .andExpect(jsonPath("$.url", is(baseUrl + "/logic-exception-translated-fields")))
                .andExpect(jsonPath("$.key", is("error.logic-exception-translated")))
                .andExpect(jsonPath("$.key_parameters").doesNotExist())
                .andExpect(jsonPath("$.time").exists())
                .andExpect(jsonPath("$.violations").isArray())
                .andExpect(jsonPath("$.violations[0].title", is("FieldError title")))
                .andExpect(jsonPath("$.violations[0].detail", is("Excepcion en campo traducida")))
                .andExpect(
                        jsonPath(
                                "$.violations[0].translation_key",
                                is("error.field.logic-exception-translated")));
    }

    @Test
    void test_exception_throwing_handle_logic_exception_translated_with_fields_en()
            throws Exception {
        mockMvc.perform(
                        get(baseUrl + "/logic-exception-translated-fields")
                                .header("Accept-Language", "en"))
                .andExpect(status().is4xxClientError())
                .andExpect(
                        header().string("Content-Type", "application/problem+json;charset=UTF-8"))
                .andDo(print())
                .andExpect(jsonPath("$.title", is("Exception title")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.detail", is("Translated exception")))
                .andExpect(jsonPath("$.front_end_code", is(2000)))
                .andExpect(jsonPath("$.url", is(baseUrl + "/logic-exception-translated-fields")))
                .andExpect(jsonPath("$.key", is("error.logic-exception-translated")))
                .andExpect(jsonPath("$.key_parameters").doesNotExist())
                .andExpect(jsonPath("$.time").exists())
                .andExpect(jsonPath("$.violations").isArray())
                .andExpect(jsonPath("$.violations[0].title", is("FieldError title")))
                .andExpect(jsonPath("$.violations[0].detail", is("Translated field exception")))
                .andExpect(
                        jsonPath(
                                "$.violations[0].translation_key",
                                is("error.field.logic-exception-translated")));
    }

    @Test
    void test_exception_throwing_and_cors_headers() throws Exception {
        mockMvc.perform(get(baseUrl + "/logic-exception"))
                .andExpect(status().is4xxClientError())
                .andExpect(
                        header().string("Content-Type", "application/problem+json;charset=UTF-8"))
                .andDo(print())
                .andExpect(jsonPath("$.title", is("Exception title")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.detail", is("Exception detail")))
                .andExpect(jsonPath("$.front_end_code", is(2000)))
                .andExpect(jsonPath("$.url", is(baseUrl + "/logic-exception")))
                .andExpect(jsonPath("$.key", is("error.logic-exception")))
                .andExpect(jsonPath("$.key_parameters").doesNotExist())
                .andExpect(jsonPath("$.time").exists())
                .andExpect(
                        header().stringValues(
                                        "Vary",
                                        "Origin",
                                        "Access-Control-Request-Method",
                                        "Access-Control-Request-Headers"));
    }

    @Test
    void test_exception_throwing_handle_missing_param() throws Exception {
        mockMvc.perform(get(baseUrl + "/missing-param"))
                .andExpect(status().is4xxClientError())
                .andExpect(
                        header().string("Content-Type", "application/problem+json;charset=UTF-8"))
                .andDo(print())
                .andExpect(jsonPath("$.title", is("Bad Request")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(
                        jsonPath(
                                "$.detail",
                                is(
                                        "Required request parameter 'key' for method parameter type String is not present")));
    }

    @Test
    void test_exception_throwing_handle_missing_param_en() throws Exception {
        mockMvc.perform(get(baseUrl + "/missing-param").header("Accept-Language", "en"))
                .andExpect(status().is4xxClientError())
                .andExpect(
                        header().string("Content-Type", "application/problem+json;charset=UTF-8"))
                .andDo(print())
                .andExpect(jsonPath("$.title", is("Bad Request")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(
                        jsonPath(
                                "$.detail",
                                is(
                                        "Required request parameter 'key' for method parameter type String is not present")));
    }

    @Test
    void test_exception_throwing_file_excedded() throws Exception {
        byte[] bytes = new byte[1024 * 1024 * 10];
        MockMultipartFile multipartFile =
                new MockMultipartFile(
                        "file", "file_exceeded_size_test.pdf", "application/pdf", bytes);

        mockMvc.perform(multipart(baseUrl + "/exceed-file-size").file(multipartFile))
                .andExpect(status().is4xxClientError())
                .andExpect(
                        header().string("Content-Type", "application/problem+json;charset=UTF-8"))
                .andDo(print())
                .andExpect(jsonPath("$.title", is("Bad Request")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.detail", is("This excedede size")));
    }

    @Test
    void test_exception_throwing_handle_validate_dto() throws JsonProcessingException, Exception {
        ValidationAnnotatedDto validationAnnotatedDto = new ValidationAnnotatedDto();

        mockMvc.perform(
                        post(baseUrl + "/validate-dto")
                                .content(objectMapper.writeValueAsString(validationAnnotatedDto))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8"))
                .andExpect(status().is4xxClientError())
                .andExpect(
                        header().string("Content-Type", "application/problem+json;charset=UTF-8"))
                .andDo(print())
                .andExpect(jsonPath("$.title", is("Constraint Violation")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.detail").doesNotExist())
                .andExpect(jsonPath("$.violations").isArray())
                .andExpect(jsonPath("$.violations[0].field", is("name")))
                .andExpect(jsonPath("$.violations[0].message", is("no debe ser nulo")));
    }

    @Test
    void test_exception_throwing_handle_validate_dto_size()
            throws JsonProcessingException, Exception {
        ValidationAnnotatedDto validationAnnotatedDto = new ValidationAnnotatedDto();
        validationAnnotatedDto.setName("123");

        mockMvc.perform(
                        post(baseUrl + "/validate-dto")
                                .content(objectMapper.writeValueAsString(validationAnnotatedDto))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8"))
                .andExpect(status().is4xxClientError())
                .andExpect(
                        header().string("Content-Type", "application/problem+json;charset=UTF-8"))
                .andDo(print())
                .andExpect(jsonPath("$.title", is("Constraint Violation")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.detail").doesNotExist())
                .andExpect(jsonPath("$.violations").isArray())
                .andExpect(jsonPath("$.violations[0].field", is("name")))
                .andExpect(jsonPath("$.violations[0].message").exists());
    }

    @Test
    void test_exception_throwing_handle_validate_dto_no_annotation()
            throws JsonProcessingException, Exception {
        ValidationAnnotatedDto validationAnnotatedDto = new ValidationAnnotatedDto();

        mockMvc.perform(
                        post(baseUrl + "/validate-dto-no-annotation")
                                .content(objectMapper.writeValueAsString(validationAnnotatedDto))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8"))
                .andExpect(status().is4xxClientError())
                .andExpect(
                        header().string("Content-Type", "application/problem+json;charset=UTF-8"))
                .andDo(print())
                .andExpect(jsonPath("$.title", is("Constraint Violation")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.detail").doesNotExist())
                .andExpect(jsonPath("$.violations").isArray())
                .andExpect(jsonPath("$.violations[0].field", is("name")))
                .andExpect(jsonPath("$.violations[0].message", is("no debe ser nulo")));
    }

    @Test
    void test_exception_throwing_handle_validate_dto_size_no_annotation()
            throws JsonProcessingException, Exception {
        ValidationAnnotatedDto validationAnnotatedDto = new ValidationAnnotatedDto();
        validationAnnotatedDto.setName("123");

        mockMvc.perform(
                        post(baseUrl + "/validate-dto-no-annotation")
                                .content(objectMapper.writeValueAsString(validationAnnotatedDto))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8"))
                .andExpect(status().is4xxClientError())
                .andExpect(
                        header().string("Content-Type", "application/problem+json;charset=UTF-8"))
                .andDo(print())
                .andExpect(jsonPath("$.title", is("Constraint Violation")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.detail").doesNotExist())
                .andExpect(jsonPath("$.violations").isArray())
                .andExpect(jsonPath("$.violations[0].field", is("name")))
                .andExpect(jsonPath("$.violations[0].message").exists());
    }

    @Test
    void test_exception_throwing_not_service_available() throws JsonProcessingException, Exception {
        mockMvc.perform(get(baseUrl + "/single-status-error-response"))
                .andExpect(
                        header().string("Content-Type", "application/problem+json;charset=UTF-8"))
                .andExpect(status().isServiceUnavailable())
                .andDo(print());
    }

    @Test
    void test_exception_throwing_not_service_available_bigger_response()
            throws JsonProcessingException, Exception {
        mockMvc.perform(get(baseUrl + "/single-status-error-bigger-response"))
                .andExpect(header().string("Content-Type", "application/json;charset=UTF-8"))
                .andExpect(status().isServiceUnavailable())
                .andDo(print());
    }

    @Test
    void test_exception_throwing_url_not_found() throws JsonProcessingException, Exception {
        mockMvc.perform(get(baseUrl + "/url-not-found"))
                .andExpect(
                        header().string("Content-Type", "application/problem+json;charset=UTF-8"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void test_exception_throwing_npe() throws JsonProcessingException, Exception {
        mockMvc.perform(get(baseUrl + "/npe"))
                .andExpect(
                        header().string("Content-Type", "application/problem+json;charset=UTF-8"))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.title", is("Internal Server Error")))
                .andExpect(jsonPath("$.status", is(500)))
                .andExpect(jsonPath("$.detail", is("NPE registered")))
                .andDo(print());
    }
}
