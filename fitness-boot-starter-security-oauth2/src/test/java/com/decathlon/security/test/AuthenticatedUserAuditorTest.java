/* Decathlon (C)2023 */
package com.decathlon.security.test;

import static org.assertj.core.api.Assertions.assertThat;

import com.decathlon.data.domain.AuthenticatedUserAuditor;
import com.decathlon.data.security.support.SecurityEvaluationContextExtension;
import com.decathlon.security.SecurityOAuth2Configuration;
import com.decathlon.security.controllers.AuthorizationRestController;
import com.decathlon.security.services.DefaultService;
import com.decathlon.security.test.context.support.WithMockedUser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@WebMvcTest(controllers = {AuthorizationRestController.class})
@Import({DefaultService.class, SecurityOAuth2Configuration.class})
@TestPropertySource(
        locations = {"classpath:application.properties", "classpath:oauth.properties"},
        properties = {"app.jackson.hibernate-module-enable=false"})
@WithMockedUser
class AuthenticatedUserAuditorTest {

    private AuthenticatedUserAuditor authenticatedUserAuditor;
    private SecurityEvaluationContextExtension securityEvaluationContextExtension;

    @BeforeEach
    void setup() {
        authenticatedUserAuditor = new AuthenticatedUserAuditor("preferred_username");
        securityEvaluationContextExtension = new SecurityEvaluationContextExtension();
    }

    @Test
    void when_claim_exists() throws Exception {
        authenticatedUserAuditor = new AuthenticatedUserAuditor("given_name");

        Optional<String> audit = authenticatedUserAuditor.getCurrentAuditor();

        assertThat(audit).isEqualTo(Optional.of("Rachel"));
    }

    @Test
    void when_claim_does_not_exist() throws Exception {
        authenticatedUserAuditor = new AuthenticatedUserAuditor("no_existing_claimg");

        Optional<String> audit = authenticatedUserAuditor.getCurrentAuditor();

        assertThat(audit).isEqualTo(Optional.of("rachel"));
    }

    @Test
    void when_evaluation_is_ok() throws Exception {
        assertThat(securityEvaluationContextExtension.getExtensionId()).isEqualTo("security");
        assertThat(securityEvaluationContextExtension.getProperties())
                .contains(Map.entry("countryCode", "ES"));
        assertThat((List<Long>) securityEvaluationContextExtension.getProperties().get("centersId"))
                .contains(1l, 2l, 3l);
    }

    @Test
    @WithMockedUser(centersId = {})
    void when_evaluation_is_ok_no_centers() throws Exception {
        assertThat(securityEvaluationContextExtension.getExtensionId()).isEqualTo("security");
        assertThat(securityEvaluationContextExtension.getProperties())
                .contains(Map.entry("countryCode", "ES"));
        assertThat((List<Long>) securityEvaluationContextExtension.getProperties().get("centersId"))
                .isEmpty();
    }
}
