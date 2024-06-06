/* AssentSoftware (C)2023 */
package com.decathlon.rest.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.decathlon.rest.config.JsonConfiguration;
import com.decathlon.rest.config.LocaleConfiguration;
import com.decathlon.rest.context.i18n.Translator;
import com.decathlon.rest.context.properties.RestConfigParameters;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {TranslationRestController.class})
@Import({
    RestConfigParameters.class,
    Translator.class,
    LocaleConfiguration.class,
    JsonConfiguration.class
})
@TestPropertySource(locations = {"classpath:rest.properties", "classpath:application.properties"})
class TranslationRestControllerTest {

    @Autowired private MockMvc mockMvc;

    @Test
    void when_locale_default() throws Exception {
        mockMvc.perform(get("/api/v1/language-test").characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.value", is("Mensaje traducido")));
    }

    @Test
    void when_locale_es() throws Exception {
        mockMvc.perform(
                        get("/api/v1/language-test")
                                .characterEncoding("utf-8")
                                .header("Accept-Language", "es"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.value", is("Mensaje traducido")));
    }

    @Test
    void when_locale_en() throws Exception {
        mockMvc.perform(
                        get("/api/v1/language-test")
                                .characterEncoding("utf-8")
                                .header("Accept-Language", "en"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.value", is("Message translated")));
    }
}
