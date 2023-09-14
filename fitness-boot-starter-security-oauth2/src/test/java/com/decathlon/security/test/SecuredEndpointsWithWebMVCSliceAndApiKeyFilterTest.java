/* Decathlon (C)2023 */
package com.decathlon.security.test;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.decathlon.security.SecurityOAuth2Configuration;
import com.decathlon.security.controllers.AuthorizationRestController;
import com.decathlon.security.services.DefaultService;
import com.decathlon.security.test.context.support.WithMockedUser;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {AuthorizationRestController.class})
@Import({DefaultService.class, SecurityOAuth2Configuration.class})
@TestPropertySource(
        locations = {"classpath:application.properties", "classpath:oauth.properties"},
        properties = {
            "app.jackson.hibernate-module-enable=false",
            "app.security.api-key=Testing api key",
            "spring.security.oauth2.resourceserver.jwt.audience=account"
        })
@WithMockedUser
class SecuredEndpointsWithWebMVCSliceAndApiKeyFilterTest {

    @Autowired private MockMvc mvc;

    @Test
    void when_secured_by_any_rule() throws Exception {
        this.mvc
                .perform(
                        get("/api/v1/protected/by-any-rule")
                                .header("X-API-KEY", "Testing api key")
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void when_secured_by_any_rule_no_apikey_header() throws Exception {
        this.mvc
                .perform(get("/api/v1/protected/by-any-rule").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.title", is("Invalid API KEY")));
    }
}
