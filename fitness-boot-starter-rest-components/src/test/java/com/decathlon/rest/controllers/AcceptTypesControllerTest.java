/* Decathlon (C)2023 */
package com.decathlon.rest.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.decathlon.rest.config.CustomWebMvcConfiguration;
import com.decathlon.rest.config.JsonConfiguration;
import com.decathlon.rest.context.properties.RestConfigParameters;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@WebMvcTest(controllers = {AcceptTypesController.class})
@Import({RestConfigParameters.class, JsonConfiguration.class, CustomWebMvcConfiguration.class})
@TestPropertySource(locations = {"classpath:rest.properties", "classpath:application.properties"})
class AcceptTypesControllerTest {

    @Autowired private MockMvc mockMvc;

    @Test
    void ok_for_json_accept_type() throws Exception {
        mockMvc.perform(
                        get("/api/v1/types-handler")
                                .param("add_nullable_value", "true")) // Without accept header
                // default to json
                .andDo(print())
                .andExpect(jsonPath("$.name", is("Harold")))
                .andExpect(header().string("Content-Type", "application/json;charset=UTF-8"));
    }

    @Test
    void ok_for_haljson_accept_type() throws Exception {
        mockMvc.perform(
                        get("/api/v1/types-handler")
                                .param("add_nullable_value", "true")
                                .accept("application/hal+json"))
                .andDo(print())
                .andExpect(jsonPath("$.name", is("Harold")))
                .andExpect(header().string("Content-Type", "application/hal+json;charset=UTF-8"));
    }

    @Test
    void ok_for_text_plain_accept_type() throws Exception {
        MvcResult textPlainResult =
                mockMvc.perform(get("/api/v1/types-handler").accept(MediaType.TEXT_PLAIN_VALUE))
                        .andDo(print())
                        .andExpect(header().string("Content-Type", "text/plain;charset=UTF-8"))
                        .andReturn();
        assertEquals("Prueba de string", textPlainResult.getResponse().getContentAsString());
    }

    @Test
    void ok_for_text_plain_format_parameter() throws Exception {
        MvcResult textPlainResult =
                mockMvc.perform(
                                get("/api/v1/types-handler")
                                        .param("format", MediaType.TEXT_PLAIN_VALUE))
                        .andDo(print())
                        .andExpect(header().string("Content-Type", "text/plain;charset=UTF-8"))
                        .andReturn();
        assertEquals("Prueba de string", textPlainResult.getResponse().getContentAsString());
    }

    @Test
    void ok_for_xml_accept_type() throws Exception {
        MvcResult xmlPlainResult =
                mockMvc.perform(
                                get("/api/v1/types-handler")
                                        .accept(MediaType.APPLICATION_XML_VALUE))
                        .andDo(print())
                        .andExpect(header().string("Content-Type", "application/xml;charset=UTF-8"))
                        .andReturn();
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><keyValueXmlResponseDto><value>Prueba</value></keyValueXmlResponseDto>",
                xmlPlainResult.getResponse().getContentAsString());
    }

    @Test
    void when_coming_from_spa_app() throws Exception {
        String jsonRequest = "{\"local_date_time_field\":\"27/11/2020T23:00:00.000Z\"}";

        mockMvc.perform(
                        post("/api/v1/types-handler/from-spa")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(header().string("Content-Type", "application/json;charset=UTF-8"))
                .andExpect(header().string("Location", "/api/v1/types-handler/from-spa/1"))
                .andExpect(jsonPath("$.local_date_time_field", is("27/11/2020T23:00:00.000Z")));
    }

    @Test
    void when_fails_url() throws Exception {
        String jsonRequest = "{\"local_date_time_field\":\"2020-11-27T23:00:00.000Z\"}";

        mockMvc.perform(
                        post("/api/v1/types-handler/from-spa-not-found-url")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(
                        header().string("Content-Type", "application/problem+json;charset=UTF-8"))
                .andExpect(jsonPath("$.title", is("Not Found")))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(
                        jsonPath(
                                "$.detail",
                                is(
                                        "No endpoint POST /api/v1/types-handler/from-spa-not-found-url.")));
    }
}
