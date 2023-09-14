/* Decathlon (C)2023 */
package com.decathlon.security.config;

import com.decathlon.security.jwt.service.UserInformationService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class UserInformationEndPointConfiguration {

    @Bean
    RestTemplate restTemplateForUserInfo() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
        restTemplate.setMessageConverters(getJsonMessageConverters());

        return restTemplate;
    }

    @Bean
    UserInformationService userInformationService(
            @Value("${spring.security.oauth2.client.provider.decathlon.user-info-uri}")
                    String userInfoUri,
            RestTemplate restTemplateForUserInfo) {
        return new UserInformationService(userInfoUri, restTemplateForUserInfo);
    }

    private List<HttpMessageConverter<?>> getJsonMessageConverters() {
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        converters.add(new MappingJackson2HttpMessageConverter());
        converters.add(new FormHttpMessageConverter());

        return converters;
    }
}
