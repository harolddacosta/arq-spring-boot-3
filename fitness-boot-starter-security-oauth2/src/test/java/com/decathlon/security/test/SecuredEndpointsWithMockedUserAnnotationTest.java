/* AssentSoftware (C)2023 */
package com.decathlon.security.test;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.decathlon.security.SecurityServicesApplication;
import com.decathlon.security.test.context.support.WithMockedUser;

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
@WithMockedUser
class SecuredEndpointsWithMockedUserAnnotationTest extends BaseJwtSecurityTest {

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
                .perform(get("/api/v1/protected/by-any-rule").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void when_secured_by_any_rule_no_jwt() throws Exception {
        this.mvc
                .perform(get("/api/v1/protected/by-any-rule").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockedUser(userName = "harold")
    void when_user_info() throws Exception {
        this.mvc
                .perform(get("/api/v1/protected/user/info").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.issued_at").exists())
                .andExpect(jsonPath("$.expires_at").exists())
                .andExpect(jsonPath("$.headers").exists())
                .andExpect(jsonPath("$.claims").exists())
                .andExpect(jsonPath("$.claims.user_name", is("harold")))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.subject").exists())
                .andExpect(jsonPath("$.issuer").exists())
                .andExpect(jsonPath("$.audience").exists())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockedUser(authorities = {"SCOPE_read"})
    void when_scope_read_exists() throws Exception {
        this.mvc
                .perform(get("/api/v1/read-only/check").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockedUser(authorities = {})
    void when_scope_read_not_exists() throws Exception {
        this.mvc
                .perform(get("/api/v1/read-only/check").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockedUser(authorities = {"ROLE_ADMIN"})
    void when_role_admin_exists() throws Exception {
        this.mvc
                .perform(get("/api/v1/role-based/check").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockedUser(authorities = {})
    void when_role_admin_not_exists() throws Exception {
        this.mvc
                .perform(get("/api/v1/role-based/check").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockedUser(authorities = {"ROLE_VIEWER"})
    void when_method_secured_role_exists() throws Exception {
        this.mvc
                .perform(
                        get("/api/v1/method-based/check_secured_annotation")
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockedUser(authorities = {})
    void when_method_secured_role_not_exists() throws Exception {
        this.mvc
                .perform(
                        get("/api/v1/method-based/check_secured_annotation")
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockedUser(authorities = {"ROLE_VIEWER"})
    void when_method_roles_allowed_exists() throws Exception {
        this.mvc
                .perform(
                        get("/api/v1/method-based/check_roles_allowed_annotation")
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockedUser(authorities = {})
    void when_method_roles_allowed_not_exists() throws Exception {
        this.mvc
                .perform(
                        get("/api/v1/method-based/check_roles_allowed_annotation")
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockedUser(authorities = {"ROLE_VIEWER"})
    void when_method_preauthorization_exists() throws Exception {
        this.mvc
                .perform(
                        get("/api/v1/method-based/check_preauthorize_annotation")
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockedUser(authorities = {})
    void when_method_preauthorization_not_exists() throws Exception {
        this.mvc
                .perform(
                        get("/api/v1/method-based/check_preauthorize_annotation")
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}
