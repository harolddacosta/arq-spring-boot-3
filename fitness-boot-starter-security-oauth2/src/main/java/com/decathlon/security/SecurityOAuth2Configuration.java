/* Decathlon (C)2023 */
package com.decathlon.security;

import com.decathlon.data.domain.AuthenticatedUserAuditor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@ComponentScan("com.decathlon.security")
@PropertySource("classpath:oauth.properties")
@EnableConfigurationProperties
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityOAuth2Configuration {

    @Bean
    AuditorAware<String> auditorAware(
            @Value("${app.security.claim-for-auditing:#{null}}") String claimForAuditing) {
        return new AuthenticatedUserAuditor(claimForAuditing);
    }

    //    @Bean
    //    EvaluationContextExtension securityExtension() {
    //        return new SecurityEvaluationContextExtension();
    //    }

    //    @Bean
    //    public DelegatingSecurityContextAsyncTaskExecutor taskExecutor(
    //            ThreadPoolTaskExecutor delegate) {
    //        return new DelegatingSecurityContextAsyncTaskExecutor(delegate);
    //    }
}
