/* Decathlon (C)2023 */
package com.decathlon.security.config;

import com.decathlon.data.domain.AuthenticatedUserAuditor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

@Configuration
public class AuditorConfiguration {

    @Bean
    AuditorAware<String> auditorAware(
            @Value("${app.security.claim-for-auditing:#{null}}") String claimForAuditing) {
        return new AuthenticatedUserAuditor(claimForAuditing);
    }
}
