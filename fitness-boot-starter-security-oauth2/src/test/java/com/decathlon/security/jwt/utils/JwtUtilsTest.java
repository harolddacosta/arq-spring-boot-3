/* Decathlon (C)2023 */
package com.decathlon.security.jwt.utils;

import static org.assertj.core.api.Assertions.assertThat;

import com.decathlon.security.SecurityOAuth2Configuration;
import com.decathlon.security.controllers.AuthorizationRestController;
import com.decathlon.security.services.DefaultService;
import com.decathlon.security.test.context.support.WithMockedUser;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.context.TestPropertySource;

@WebMvcTest(controllers = {AuthorizationRestController.class})
@Import({DefaultService.class, SecurityOAuth2Configuration.class})
@WithMockedUser(aud = "test")
@TestPropertySource(properties = {"spring.security.oauth2.resourceserver.jwt.audiences=test"})
class JwtUtilsTest {

    @Test
    void when_JwtAuthenticationToken_exists() {
        JwtAuthenticationToken authenticationToken = JwtUtils.getJwtAuthenticationToken();

        assertThat(authenticationToken).isNotNull();
        assertThat(authenticationToken.getName()).isEqualTo("rachel");
    }

    @Test
    void when_JwtAuthenticatedUser_exists() {
        Jwt authenticationToken = JwtUtils.getJwtAuthenticatedUser();

        assertThat(authenticationToken).isNotNull();
        assertThat(authenticationToken.getSubject()).isEqualTo("rachel");
    }

    @Test
    void when_RootCenterId_exists() {
        Long authenticationToken = JwtUtils.getRootCenterId();

        assertThat(authenticationToken).isNotNull().isEqualTo(1);
    }

    @Test
    void when_CountryCode_exists() {
        String authenticationToken = JwtUtils.getCountryCode();

        assertThat(authenticationToken).isNotNull().isEqualTo("ES");
    }

    @Test
    void when_CentersIds_exists() {
        Long[] authenticationToken = JwtUtils.getCentersIds();

        assertThat(authenticationToken).isNotNull();
        assertThat(authenticationToken[0]).isEqualTo(1);
    }

    @Test
    void when_CustomClaim_exists() {
        String authenticationToken = JwtUtils.getCustomClaim("sub");

        assertThat(authenticationToken).isNotNull().isEqualTo("rachel");
    }

    @Test
    void when_hasAccess_exists() {
        boolean authenticationToken = JwtUtils.hasAccess("SCOPE_read");

        assertThat(authenticationToken).isTrue();
    }

    @Test
    void when_hasNoAccess_exists() {
        boolean authenticationToken = JwtUtils.hasAccess("NON_EXISTING_ROLE");

        assertThat(authenticationToken).isFalse();
    }
}
