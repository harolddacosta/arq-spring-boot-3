/* Decathlon (C)2023 */
package com.decathlon.rest.crud;

import com.decathlon.rest.RestServicesConfiguration;
import com.decathlon.rest.http.client.LocaleHeaderInterceptor;
import com.decathlon.rest.web.client.CustomResponseErrorHandler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@Import({RestServicesConfiguration.class})
public class TestRestServicesApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestRestServicesApplication.class, args);
    }

    @Bean
    RestTemplate restTemplate(
            MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter) {
        RestTemplate restTemplate = new RestTemplate();

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        messageConverters.add(mappingJackson2HttpMessageConverter);

        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        restTemplate.setErrorHandler(
                new CustomResponseErrorHandler(
                        mappingJackson2HttpMessageConverter.getObjectMapper()));
        restTemplate.setMessageConverters(messageConverters);
        restTemplate.getInterceptors().add(new LocaleHeaderInterceptor());

        return restTemplate;
    }
}
