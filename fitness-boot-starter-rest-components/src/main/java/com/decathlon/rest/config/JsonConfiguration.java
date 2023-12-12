/* Decathlon (C)2023 */
package com.decathlon.rest.config;

import com.decathlon.rest.context.properties.RestConfigParameters;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.RequiredArgsConstructor;

import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.zalando.problem.jackson.ProblemModule;
import org.zalando.problem.violations.ConstraintViolationProblemModule;

import java.time.format.DateTimeFormatter;

@Configuration
@RequiredArgsConstructor
public class JsonConfiguration {

    private final RestConfigParameters restConfigParameters;

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

    @Bean
    JsonNullableModule jsonNullableModule() {
        return new JsonNullableModule();
    }

    @Bean
    Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return this::configureSerializers;
    }

    private void configureSerializers(Jackson2ObjectMapperBuilder builder) {
        builder.serializers(
                new LocalDateSerializer(
                        DateTimeFormatter.ofPattern(
                                restConfigParameters.getDates().getDateFormat())),
                new LocalDateTimeSerializer(
                        DateTimeFormatter.ofPattern(
                                restConfigParameters.getDates().getDateTimeFormat())));
        builder.deserializers(
                new LocalDateDeserializer(
                        DateTimeFormatter.ofPattern(
                                restConfigParameters.getDates().getDateFormat())),
                new LocalDateTimeDeserializer(
                        DateTimeFormatter.ofPattern(
                                restConfigParameters.getDates().getDateTimeFormat())));
    }
}
