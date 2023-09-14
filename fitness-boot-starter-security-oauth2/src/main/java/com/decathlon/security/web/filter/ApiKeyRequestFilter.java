/* Decathlon (C)2023 */
package com.decathlon.security.web.filter;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.ThrowableProblem;

import java.io.IOException;
import java.util.Optional;

@Slf4j
public class ApiKeyRequestFilter extends OncePerRequestFilter {

    private static final String HEADER_X_API_KEY = "X-API-KEY";
    private final String apiKey;
    private final ObjectMapper mapper;

    public ApiKeyRequestFilter(String apiKey, ObjectMapper mapper) {
        this.apiKey = apiKey;
        this.mapper = mapper;

        log.info(
                "The 'ApiKeyRequestFilter' has been activated, checking for '{}' header",
                HEADER_X_API_KEY);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            return true;
        }

        return false;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Optional<String> keyHeader = Optional.ofNullable(request.getHeader(HEADER_X_API_KEY));

        if (keyHeader.isPresent() && apiKey.equalsIgnoreCase(keyHeader.get())) {
            filterChain.doFilter(request, response);
        } else {
            ThrowableProblem apiKeyProblemBuilder =
                    Problem.builder()
                            .withTitle("Invalid API KEY")
                            .withStatus(Status.UNAUTHORIZED)
                            .withDetail(
                                    "You either need to provide a 'X-API-KEY' header with the request or the current header value is wrong")
                            .build();

            response.setContentType("application/problem+json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            mapper.writeValue(response.getWriter(), apiKeyProblemBuilder);
        }
    }
}
