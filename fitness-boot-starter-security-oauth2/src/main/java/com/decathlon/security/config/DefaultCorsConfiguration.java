/* Decathlon (C)2023 */
package com.decathlon.security.config;

import com.decathlon.security.context.properties.SecurityConfigParameters;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.web.servlet.ConditionalOnMissingFilterBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Configuration
public class DefaultCorsConfiguration {

    @Bean
    @ConditionalOnMissingFilterBean(value = CorsFilter.class)
    CorsConfigurationSource corsConfigurationSource(SecurityConfigParameters configParameters) {
        log.info("Configuring the CorsConfigurationSource as the CorsFilter is not present");

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(
                Arrays.asList(
                        StringUtils.split(configParameters.getCors().getAllowedOrigins(), ',')));
        configuration.setAllowedMethods(
                Arrays.asList(
                        StringUtils.split(configParameters.getCors().getAllowedMethods(), ',')));
        configuration.setAllowedHeaders(
                Arrays.asList(
                        StringUtils.split(configParameters.getCors().getAllowedHeaders(), ',')));
        configuration.setExposedHeaders(List.of("*"));
        configuration.setAllowCredentials(false);

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(configParameters.getCors().getPath(), configuration);

        return source;
    }
}
