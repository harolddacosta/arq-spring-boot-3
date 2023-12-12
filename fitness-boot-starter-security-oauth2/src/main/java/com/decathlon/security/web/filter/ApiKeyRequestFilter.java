/* Decathlon (C)2023 */
package com.decathlon.security.web.filter;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.ThrowableProblem;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
public class ApiKeyRequestFilter extends OncePerRequestFilter {

    private static final String HEADER_X_API_KEY = "X-API-KEY";

    private final String apiKey;
    private final ObjectMapper mapper;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return SecurityContextHolder.getContext().getAuthentication() == null;
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
