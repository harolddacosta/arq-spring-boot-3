/* Decathlon (C)2023 */
package com.decathlon.rest.config;

import com.decathlon.rest.http.client.LocaleHeaderInterceptor;
import com.decathlon.rest.web.client.CustomResponseErrorHandler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfiguration {

    @Bean
    RestTemplate restTemplate(
            MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter) {
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.setErrorHandler(
                new CustomResponseErrorHandler(
                        mappingJackson2HttpMessageConverter.getObjectMapper()));
        restTemplate.getInterceptors().add(new LocaleHeaderInterceptor());
        restTemplate.setRequestFactory(
                new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));

        return restTemplate;
    }
}
