/* AssentSoftware (C)2023 */
package com.decathlon.rest.http.client;

import lombok.extern.slf4j.Slf4j;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
public class LocaleHeaderInterceptor implements ClientHttpRequestInterceptor {

    private static final String HEADER_ACCEPT_LANGUAGE = "Accept-Language";

    @Override
    public ClientHttpResponse intercept(
            HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {
        log.trace(
                "Adding {} '{}' header to request",
                HEADER_ACCEPT_LANGUAGE,
                LocaleContextHolder.getLocale().getLanguage());

        request.getHeaders()
                .setAcceptLanguageAsLocales(Arrays.asList(LocaleContextHolder.getLocale()));

        return execution.execute(request, body);
    }
}
