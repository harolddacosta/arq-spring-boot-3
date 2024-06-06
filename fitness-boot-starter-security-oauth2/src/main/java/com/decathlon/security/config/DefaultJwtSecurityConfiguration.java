/* AssentSoftware (C)2023 */
package com.decathlon.security.config;

import static org.springframework.security.config.Customizer.withDefaults;

import com.decathlon.security.authentication.M2MJwtAuthenticationManagerResolver;
import com.decathlon.security.jwt.converters.ResourceRolesConverter;
import com.decathlon.security.web.filter.ApiKeyRequestFilter;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.lang.Nullable;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

import java.util.Arrays;

@Import(SecurityProblemSupport.class)
@Slf4j
@RequiredArgsConstructor
public class DefaultJwtSecurityConfiguration {

    private static final String[] AUTH_WHITELIST = {
        "/v2/api-docs",
        "/configuration/**",
        "/swagger-resources/**",
        "/swagger-ui.html",
        "/webjars/**",
        "/api-docs/**",
        "/performance-monitor/**",
        "/swagger-ui/**",
        "/v3/**",
        "/v1/ping"
    };

    @Autowired private SecurityProblemSupport problemSupport;
    @Autowired private ObjectMapper mapper;

    @Autowired private @Nullable M2MJwtAuthenticationManagerResolver authenticationManagerResolver;

    @Value("${spring.security.oauth2.resourceserver.jwt.resource-access-name}")
    protected String resourceAccessName;

    @Value("${app.security.api-key:#{null}}")
    private String apiKey;

    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

    protected final HttpSecurity preconfigureSecurityFilterChain(
            HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
        http.cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable) // NOSONAR as we use stateless apis
                .httpBasic(AbstractHttpConfigurer::disable)
                .exceptionHandling(
                        exceptionHandling ->
                                exceptionHandling
                                        .authenticationEntryPoint(problemSupport)
                                        .accessDeniedHandler(problemSupport))
                .sessionManagement(
                        sessionManagement ->
                                sessionManagement.sessionCreationPolicy(
                                        SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        requests -> {
                            Arrays.asList(AUTH_WHITELIST)
                                    .forEach(
                                            authUrl ->
                                                    requests.requestMatchers(mvc.pattern(authUrl))
                                                            .permitAll());

                            requests.requestMatchers(EndpointRequest.to(HealthEndpoint.class))
                                    .permitAll();
                        });

        if (authenticationManagerResolver != null) {
            http.oauth2ResourceServer(
                    oauth2 -> oauth2.authenticationManagerResolver(authenticationManagerResolver));
        } else {
            http.oauth2ResourceServer(
                    oauth2 ->
                            oauth2.jwt(
                                    jwt ->
                                            jwt.jwtAuthenticationConverter(
                                                    new ResourceRolesConverter(
                                                            resourceAccessName))));
        }

        if (StringUtils.isNotBlank(apiKey)) {
            log.info("ApiKeyRequestFilter enabled");

            http.addFilterAfter(
                    new ApiKeyRequestFilter(apiKey, mapper), BearerTokenAuthenticationFilter.class);
        }

        return http;
    }
}
