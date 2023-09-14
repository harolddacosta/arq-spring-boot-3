/* Decathlon (C)2023 */
package com.decathlon.security.config;

import com.decathlon.security.jwt.converters.ResourceRolesConverter;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;

@Configuration
@EnableAutoConfiguration(exclude = ErrorMvcAutoConfiguration.class)
@Slf4j
public class JwtSecurityConfiguration extends DefaultJwtSecurityConfiguration {

    @Bean
    SecurityFilterChain configureSecurityFilterChain(
            HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
        super.preconfigureSecurityFilterChain(http, mvc);

        log.trace("The resourceAccessName configured is:{}", resourceAccessName);

        http.oauth2ResourceServer(
                oauth2 ->
                        oauth2.jwt(
                                jwt ->
                                        jwt.jwtAuthenticationConverter(
                                                new ResourceRolesConverter(resourceAccessName))));

        http.authorizeHttpRequests(
                authz ->
                        authz.requestMatchers(mvc.pattern(HttpMethod.GET, "/api/v1/public/**"))
                                .permitAll()
                                .requestMatchers(
                                        mvc.pattern(HttpMethod.GET, "/api/v1/read-only/**"))
                                .hasAuthority("SCOPE_read")
                                .requestMatchers(
                                        mvc.pattern(HttpMethod.GET, "/api/v1/role-based/**"))
                                .hasAnyRole("ADMIN")
                                .anyRequest()
                                .authenticated());

        return http.build();
    }
}
