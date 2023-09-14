/* Decathlon (C)2023 */
package com.decathlon.rest.context.i18n;

import lombok.RequiredArgsConstructor;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class Translator {

    private final ResourceBundleMessageSource messageSource;

    public String toLocale(String msg) {
        return messageSource.getMessage(msg, null, LocaleContextHolder.getLocale());
    }

    public String toLocale(String msg, Locale locale) {
        return messageSource.getMessage(msg, null, locale);
    }

    public String toLocale(String msg, Object[] args) {
        return messageSource.getMessage(msg, args, LocaleContextHolder.getLocale());
    }

    public String toLocale(String msg, Object[] args, Locale locale) {
        return messageSource.getMessage(msg, args, locale);
    }
}
