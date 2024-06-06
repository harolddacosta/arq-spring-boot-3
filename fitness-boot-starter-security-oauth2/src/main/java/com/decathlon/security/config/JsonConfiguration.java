/* AssentSoftware (C)2023 */
package com.decathlon.security.config;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.problem.jackson.ProblemModule;
import org.zalando.problem.violations.ConstraintViolationProblemModule;

@Configuration
@ConditionalOnMissingClass(value = "com.decathlon.rest.config.JsonConfiguration")
@EnableAutoConfiguration(exclude = ErrorMvcAutoConfiguration.class)
public class JsonConfiguration {

    @Bean
    ProblemModule problemModule() {
        return new ProblemModule();
    }

    @Bean
    ConstraintViolationProblemModule constraintViolationProblemModule() {
        return new ConstraintViolationProblemModule();
    }

    @Bean
    JavaTimeModule javaTimeModule() {
        return new JavaTimeModule();
    }
}
