/* Decathlon (C)2023 */
package com.decathlon.rest.utils;

import jakarta.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.zalando.problem.ProblemBuilder;

import java.net.URI;

@Slf4j
public class RequestUtils {

    private RequestUtils() {
        // Utility class
    }

    public static HttpServletRequest getCurrentHttpRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        log.trace("Type of the HttpServletRequest:{}", requestAttributes);

        if (requestAttributes instanceof ServletRequestAttributes) {
            return ((ServletRequestAttributes) requestAttributes).getRequest();
        }

        return null;
    }

    public static String getCurrentUrl() {
        HttpServletRequest request = RequestUtils.getCurrentHttpRequest();

        return request != null ? request.getRequestURI() : null;
    }

    public static void addRequestAdditionalInformation(ProblemBuilder problemBuilder) {
        HttpServletRequest currentRequest = RequestUtils.getCurrentHttpRequest();

        if (currentRequest != null) {
            problemBuilder.with("url", URI.create(currentRequest.getRequestURI()));
        }
    }

    public static MultiValueMap<String, String> buildResourceCreationHeaders(Long id) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Location", RequestUtils.getCurrentUrl() + "/" + id);

        return headers;
    }
}
