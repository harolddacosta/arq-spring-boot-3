/* Decathlon (C)2023 */
package com.decathlon.security.http.client;

import com.decathlon.security.SecurityConstants;
import com.decathlon.security.jwt.utils.JwtUtils;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

@Slf4j
public class AuthorizationHeaderInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(
            HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {
        if (JwtUtils.getJwtAuthenticationToken() == null) {
            return execution.execute(request, body);
        }

        log.trace("Adding {} header to request", SecurityConstants.HEADER_AUTHORIZATION);

        String headerAuthValue = JwtUtils.getJwtAuthenticationToken().getToken().getTokenValue();

        request.getHeaders()
                .add(
                        SecurityConstants.HEADER_AUTHORIZATION,
                        SecurityConstants.TOKEN_PREFIX + headerAuthValue);

        return execution.execute(request, body);
    }
}
