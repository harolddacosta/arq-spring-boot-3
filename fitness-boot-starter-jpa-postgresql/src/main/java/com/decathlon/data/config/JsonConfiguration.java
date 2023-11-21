/* Decathlon (C)2023 */
package com.decathlon.data.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.zalando.problem.jackson.ProblemModule;
import org.zalando.problem.violations.ConstraintViolationProblemModule;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConditionalOnMissingClass(
        value = {
            "com.decathlon.rest.config.JsonConfiguration",
            "com.decathlon.security.config.JsonConfiguration"
        })
@EnableAutoConfiguration(exclude = ErrorMvcAutoConfiguration.class)
public class JsonConfiguration {

    @Bean
    @Primary
    Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        List<Module> modules = new ArrayList<>();

        // checkHibernateModuleIsPresent(modules);
        modules.add(new ProblemModule());
        modules.add(new ConstraintViolationProblemModule());
        modules.add(new JavaTimeModule());

        builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        builder.serializationInclusion(JsonInclude.Include.NON_NULL);
        builder.propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        builder.modulesToInstall(modules.toArray(Module[]::new));

        return builder;
    }
}
