/* Decathlon (C)2023 */
package com.decathlon.rest.config;

import com.decathlon.rest.context.properties.RestConfigParameters;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.RequiredArgsConstructor;

import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.zalando.problem.jackson.ProblemModule;
import org.zalando.problem.violations.ConstraintViolationProblemModule;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class JsonConfiguration {

    private final RestConfigParameters restConfigParameters;

    @Bean
    @Primary
    Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        List<Module> modules = new ArrayList<>();

        modules.add(new ProblemModule());
        modules.add(new ConstraintViolationProblemModule());
        modules.add(new JavaTimeModule());
        modules.add(new JsonNullableModule());
        configureSerializers(builder);

        builder.simpleDateFormat(restConfigParameters.getDates().getDateTimeFormat());
        builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        builder.serializationInclusion(JsonInclude.Include.NON_NULL);
        builder.propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        builder.modulesToInstall(modules.toArray(Module[]::new));

        return builder;
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
