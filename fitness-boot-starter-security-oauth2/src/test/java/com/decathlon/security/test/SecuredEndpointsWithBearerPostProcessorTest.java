/* Decathlon (C)2023 */
package com.decathlon.security.test;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.decathlon.security.SecurityServicesApplication;
import com.decathlon.security.test.utils.JwtBuilder;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(
        classes = {SecurityServicesApplication.class},
        webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
class SecuredEndpointsWithBearerPostProcessorTest extends BaseJwtSecurityTest {

    @Autowired private MockMvc mvc;

    @Test
    void when_is_public_access() throws Exception {
        this.mvc
                .perform(get("/api/v1/public/no-jwt-needed").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void when_is_public_access_auth_white_list() throws Exception {
        this.mvc
                .perform(get("/swagger-ui.html").accept(MediaType.TEXT_HTML))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void when_secured_by_any_rule() throws Exception {
        this.mvc
                .perform(
                        get("/api/v1/protected/by-any-rule")
                                .with(
                                        bearerToken(
                                                JwtBuilder.getInstance()
                                                        .secretKey(secretKey)
                                                        .build()))
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void when_secured_by_any_rule_no_jwt() throws Exception {
        this.mvc
                .perform(get("/api/v1/protected/by-any-rule").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void when_user_info() throws Exception {
        String jwt =
                JwtBuilder.getInstance()
                        .secretKey(secretKey)
                        .authorizeRoles(new String[] {"ADMIN"})
                        .userName("harold")
                        .build();

        this.mvc
                .perform(
                        get("/api/v1/protected/user/info")
                                .with(bearerToken(jwt))
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.token_value", is(jwt)))
                .andExpect(jsonPath("$.issued_at").exists())
                .andExpect(jsonPath("$.expires_at").exists())
                .andExpect(jsonPath("$.headers").exists())
                .andExpect(jsonPath("$.claims").exists())
                .andExpect(jsonPath("$.claims.user_name", is("harold")))
                .andExpect(
                        jsonPath("$.claims.resource_access.employee-service.roles[0]", is("ADMIN")))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.subject").exists())
                .andExpect(jsonPath("$.issuer").exists())
                .andExpect(jsonPath("$.audience").exists())
                .andExpect(status().isOk());
    }

    @Test
    void when_scope_read_exists() throws Exception {
        this.mvc
                .perform(
                        get("/api/v1/read-only/check")
                                .with(
                                        bearerToken(
                                                JwtBuilder.getInstance()
                                                        .secretKey(secretKey)
                                                        .scope(new String[] {"read"})
                                                        .build()))
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void when_scope_read_not_exists() throws Exception {
        this.mvc
                .perform(
                        get("/api/v1/read-only/check")
                                .with(
                                        bearerToken(
                                                JwtBuilder.getInstance()
                                                        .secretKey(secretKey)
                                                        .build()))
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void when_role_admin_exists() throws Exception {
        this.mvc
                .perform(
                        get("/api/v1/role-based/check")
                                .with(
                                        bearerToken(
                                                JwtBuilder.getInstance()
                                                        .secretKey(secretKey)
                                                        .authorizeRoles(new String[] {"ADMIN"})
                                                        .build()))
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void when_role_admin_not_exists() throws Exception {
        this.mvc
                .perform(
                        get("/api/v1/role-based/check")
                                .with(
                                        bearerToken(
                                                JwtBuilder.getInstance()
                                                        .secretKey(secretKey)
                                                        .build()))
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void when_method_secured_role_exists() throws Exception {
        this.mvc
                .perform(
                        get("/api/v1/method-based/check_secured_annotation")
                                .with(
                                        bearerToken(
                                                JwtBuilder.getInstance()
                                                        .secretKey(secretKey)
                                                        .authorizeRoles(new String[] {"VIEWER"})
                                                        .build()))
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void when_method_secured_role_not_exists() throws Exception {
        this.mvc
                .perform(
                        get("/api/v1/method-based/check_secured_annotation")
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void when_method_roles_allowed_exists() throws Exception {
        this.mvc
                .perform(
                        get("/api/v1/method-based/check_roles_allowed_annotation")
                                .with(
                                        bearerToken(
                                                JwtBuilder.getInstance()
                                                        .secretKey(secretKey)
                                                        .authorizeRoles(new String[] {"VIEWER"})
                                                        .build()))
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void when_method_roles_allowed_not_exists() throws Exception {
        this.mvc
                .perform(
                        get("/api/v1/method-based/check_roles_allowed_annotation")
                                .with(
                                        bearerToken(
                                                JwtBuilder.getInstance()
                                                        .secretKey(secretKey)
                                                        .build()))
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void when_method_preauthorization_exists() throws Exception {
        this.mvc
                .perform(
                        get("/api/v1/method-based/check_preauthorize_annotation")
                                .with(
                                        bearerToken(
                                                JwtBuilder.getInstance()
                                                        .secretKey(secretKey)
                                                        .authorizeRoles(new String[] {"VIEWER"})
                                                        .build()))
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void when_method_preauthorization_not_exists() throws Exception {
        this.mvc
                .perform(
                        get("/api/v1/method-based/check_preauthorize_annotation")
                                .with(
                                        bearerToken(
                                                JwtBuilder.getInstance()
                                                        .secretKey(secretKey)
                                                        .build()))
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void when_jwt_is_expired() throws Exception {
        this.mvc
                .perform(
                        get("/api/v1/protected/by-any-rule")
                                .with(
                                        bearerToken(
                                                JwtBuilder.getInstance()
                                                        .secretKey(secretKey)
                                                        .exp(-4)
                                                        .iat(-5)
                                                        .build()))
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void when_signature_is_wrong() throws Exception {
        this.mvc
                .perform(
                        get("/api/v1/protected/by-any-rule")
                                .with(
                                        bearerToken(
                                                JwtBuilder.getInstance()
                                                        .secretKey("NO_VALID_SECRET_KEY")
                                                        .build()))
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
