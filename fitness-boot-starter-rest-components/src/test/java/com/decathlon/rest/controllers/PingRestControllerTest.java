/* Decathlon (C)2023 */
package com.decathlon.rest.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.decathlon.rest.config.JsonConfiguration;
import com.decathlon.rest.config.LocaleConfiguration;
import com.decathlon.rest.context.i18n.Translator;
import com.decathlon.rest.context.properties.RestConfigParameters;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {PingController.class})
@Import({
    RestConfigParameters.class,
    Translator.class,
    LocaleConfiguration.class,
    JsonConfiguration.class
})
@TestPropertySource(
        locations = {"classpath:application.properties", "classpath:rest.properties"},
        properties = {"server.servlet.context-path=/api"})
class PingRestControllerTest {

    @MockBean HealthEndpoint healthEnpoint;

    @Autowired private MockMvc mockMvc;

    @Test
    void when_locale_default() throws Exception {
        when(healthEnpoint.health()).thenReturn(Health.down().build());

        mockMvc.perform(get("/v1/ping").characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
