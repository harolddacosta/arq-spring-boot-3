/* Decathlon (C)2023 */
package com.decathlon.rest.config;

import com.decathlon.rest.context.properties.RestConfigParameters;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableAutoConfiguration(exclude = ErrorMvcAutoConfiguration.class)
public class CustomWebMvcConfiguration implements WebMvcConfigurer {

    @Autowired private RestConfigParameters restConfigParameters;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(restConfigParameters.getCors().getPath())
                .allowedOrigins(
                        StringUtils.split(restConfigParameters.getCors().getAllowedOrigins(), ','))
                .allowedMethods(
                        StringUtils.split(restConfigParameters.getCors().getAllowedMethods(), ','))
                .allowedHeaders(
                        StringUtils.split(restConfigParameters.getCors().getAllowedHeaders(), ','))
                .exposedHeaders("*")
                .allowCredentials(false)
                .maxAge(3600);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/public/", "classpath:/public/static/");
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer
                .favorParameter(true)
                .defaultContentType(MediaType.APPLICATION_JSON)
                .mediaType("text/plain", MediaType.TEXT_PLAIN);
    }
}
