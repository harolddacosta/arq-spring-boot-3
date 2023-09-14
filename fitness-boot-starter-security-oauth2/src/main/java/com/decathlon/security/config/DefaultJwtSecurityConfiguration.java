/* Decathlon (C)2023 */
package com.decathlon.security.config;

import static org.springframework.security.config.Customizer.withDefaults;

import com.decathlon.security.jwt.converters.ResourceRolesConverter;
import com.decathlon.security.web.filter.ApiKeyRequestFilter;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

import java.util.Arrays;

@Import(SecurityProblemSupport.class)
public class DefaultJwtSecurityConfiguration {

    private static final String[] AUTH_WHITELIST = {
        "/v2/api-docs",
        "/configuration/**",
        "/swagger-resources/**",
        "/swagger-ui.html",
        "/webjars/**",
        "/api-docs/**",
        "/performance-monitor/**",
        "/actuator/**",
        "/swagger-ui/**",
        "/v3/**",
        "/v1/ping"
    };

    @Autowired private SecurityProblemSupport problemSupport;

    @Value("${spring.security.oauth2.resourceserver.jwt.resource-access-name}")
    protected String resourceAccessName;

    @Value("${app.security.api-key:#{null}}")
    private String apiKey;

    @Autowired private ObjectMapper mapper;

    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

    protected final HttpSecurity preconfigureSecurityFilterChain(
            HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
        http.cors(withDefaults())
                .csrf(csrf -> csrf.disable()) // NOSONAR as we use stateless apis
                .httpBasic(httpBasic -> httpBasic.disable())
                .exceptionHandling(
                        exceptionHandling ->
                                exceptionHandling
                                        .authenticationEntryPoint(problemSupport)
                                        .accessDeniedHandler(problemSupport))
                .oauth2ResourceServer(
                        oauth2 ->
                                oauth2.jwt(
                                        jwt ->
                                                jwt.jwtAuthenticationConverter(
                                                        new ResourceRolesConverter(
                                                                resourceAccessName))))
                .sessionManagement(
                        sessionManagement ->
                                sessionManagement.sessionCreationPolicy(
                                        SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        requests -> {
                            Arrays.asList(AUTH_WHITELIST)
                                    .forEach(
                                            authUrl -> {
                                                requests.requestMatchers(mvc.pattern(authUrl))
                                                        .permitAll();
                                            });
                        });

        if (StringUtils.isNotBlank(apiKey)) {
            http.addFilterAfter(
                    new ApiKeyRequestFilter(apiKey, mapper), BearerTokenAuthenticationFilter.class);
        }

        return http;
    }
}
