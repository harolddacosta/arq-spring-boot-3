/* Decathlon (C)2023 */
package com.decathlon.rest.config;

import com.decathlon.rest.context.properties.RestConfigParameters;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

@Configuration
public class LocaleConfiguration {

    // Do not rename that bean, otherwise Spring MVC won't pick it up!
    @Bean("localeResolver")
    LocaleResolver acceptHeaderLocaleResolver(RestConfigParameters restConfigParameters) {
        AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();

        List<Locale> supportedLocales =
                Stream.of(restConfigParameters.getLocales().getSupportedLocales())
                        .map(supportedLocale -> new Locale(supportedLocale))
                        .toList();

        resolver.setDefaultLocale(new Locale(restConfigParameters.getLocales().getDefaultLocale()));
        resolver.setSupportedLocales(supportedLocales);

        return resolver;
    }

    @Bean(name = "messageSource")
    ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("i18n.messages");
        messageSource.setDefaultEncoding("UTF-8");

        return messageSource;
    }
}
