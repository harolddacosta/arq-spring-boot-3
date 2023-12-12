/* Decathlon (C)2023 */
package com.decathlon.openapi;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.servers.Server;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Arrays;

@Configuration
@ComponentScan("com.decathlon.openapi")
@PropertySource("classpath:openapi.properties")
public class OpenApiConfiguration {

    private static final String SECURITY_SCHEMA_NAME_JWT = "JWT";
    private static final String SECURITY_SCHEMA_NAME_API_KEY_AUTH = "ApiKeyAuth";

    @Bean
    ModelResolver modelResolver(ObjectMapper objectMapper) {
        return new ModelResolver(objectMapper);
    }

    @Bean
    @ConditionalOnProperty(
            prefix = "app",
            name = "open-api-bean.enabled",
            havingValue = "true",
            matchIfMissing = true)
    OpenAPI customOpenAPI(
            @Value("${springdoc.version}") String appVersion,
            @Value("${springdoc.api-title}") String apiTitle,
            @Value("${app.swagger.server-paths}") String[] swaggerServerPaths) {
        Scopes scopes = new Scopes();
        scopes.addString("read", "apis");

        SecurityRequirement securityRequirement = new SecurityRequirement();
        securityRequirement.addList(SECURITY_SCHEMA_NAME_API_KEY_AUTH, "[read]");
        securityRequirement.addList(SECURITY_SCHEMA_NAME_JWT, "[read]");

        SecurityScheme apiKeyScheme =
                new SecurityScheme()
                        .name(SECURITY_SCHEMA_NAME_API_KEY_AUTH)
                        .type(SecurityScheme.Type.APIKEY)
                        .in(In.HEADER)
                        .name("X-API-KEY");

        SecurityScheme bearerSchema =
                new SecurityScheme()
                        .name(SECURITY_SCHEMA_NAME_JWT)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat(SECURITY_SCHEMA_NAME_JWT);

        OpenAPI openAPI = new OpenAPI();
        Arrays.asList(swaggerServerPaths)
                .forEach(path -> openAPI.addServersItem(new Server().url(path)));

        return openAPI.components(
                        new Components()
                                .addSecuritySchemes(SECURITY_SCHEMA_NAME_API_KEY_AUTH, apiKeyScheme)
                                .addSecuritySchemes(SECURITY_SCHEMA_NAME_JWT, bearerSchema))
                .addSecurityItem(securityRequirement)
                .info(
                        new Info()
                                .title(apiTitle)
                                .version(appVersion)
                                .license(
                                        new License()
                                                .name("Apache 2.0")
                                                .url("http://springdoc.org")));
    }
}
