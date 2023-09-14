/* Decathlon (C)2023 */
package com.decathlon.openapi;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
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

@Configuration
@ComponentScan("com.decathlon.openapi")
@PropertySource("classpath:openapi.properties")
public class OpenApiConfiguration {

    @Bean
    ModelResolver modelResolver(ObjectMapper objectMapper) {
        return new ModelResolver(objectMapper);
    }

    @Bean
    @ConditionalOnProperty(
            prefix = "app",
            name = "open-api-bean.enabled",
            matchIfMissing = true,
            havingValue = "true")
    OpenAPI customOpenAPI(
            @Value("${springdoc.version}") String appVersion,
            @Value("${spring.security.oauth2.client.access-token-uri}") String tokenUrl,
            @Value("${spring.security.oauth2.client.authorization-url}") String authorizationUrl,
            @Value("${app.swagger.server-path}") String swaggerServerPath) {
        Scopes scopes = new Scopes();
        scopes.addString("read", "apis");

        OAuthFlow AuthorizationFlow = new OAuthFlow();
        AuthorizationFlow.setTokenUrl(tokenUrl);
        AuthorizationFlow.setScopes(scopes);
        AuthorizationFlow.setAuthorizationUrl(authorizationUrl);

        OAuthFlows authFlows = new OAuthFlows();
        authFlows.setAuthorizationCode(AuthorizationFlow);

        SecurityRequirement securityRequirement = new SecurityRequirement();
        securityRequirement.addList("ApiKeyAuth", "[read]");
        securityRequirement.addList("jwt", "[read]");

        SecurityScheme apiKeyScheme =
                new SecurityScheme()
                        .type(SecurityScheme.Type.APIKEY)
                        .in(In.HEADER)
                        .name("X-API-KEY");

        SecurityScheme bearerSchema =
                new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT");

        return new OpenAPI()
                .addServersItem(new Server().url(swaggerServerPath))
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        "OAuth2",
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.OAUTH2)
                                                .flows(authFlows))
                                .addSecuritySchemes("ApiKeyAuth", apiKeyScheme)
                                .addSecuritySchemes("jwt", bearerSchema))
                .addSecurityItem(securityRequirement)
                .info(
                        new Info()
                                .title("Rest Service API")
                                .version(appVersion)
                                .license(
                                        new License()
                                                .name("Apache 2.0")
                                                .url("http://springdoc.org")));
    }
}
