/* Decathlon (C)2023 */
package com.decathlon.logging.config;

import com.decathlon.logging.filters.CorrelationMDCInjectionFilter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.zalando.logbook.CorrelationId;

@Configuration
public class CorrelationLoggingConfiguration {

    @Bean
    @Order
    FilterRegistrationBean<CorrelationMDCInjectionFilter>
            correlationIdMDCInjectionFilterRegistration(
                    CorrelationMDCInjectionFilter correlationIdMDCInjectionFilter,
                    @Value("${app.logging.correlation.filter.url}") String correlationFilterUrl) {
        FilterRegistrationBean<CorrelationMDCInjectionFilter> contextFilter =
                new FilterRegistrationBean<>();
        contextFilter.setFilter(correlationIdMDCInjectionFilter);
        contextFilter.addUrlPatterns(correlationFilterUrl);

        return contextFilter;
    }

    @Bean
    CorrelationMDCInjectionFilter correlationIdMDCInjectionFilter(CorrelationId correlationId) {
        return new CorrelationMDCInjectionFilter(correlationId); // now this is a Spring bean
    }
}
