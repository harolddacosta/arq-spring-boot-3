/* Decathlon (C)2023 */
package com.decathlon.logging.filters;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.zalando.logbook.CorrelationId;

import java.io.IOException;
import java.security.Principal;

@RequiredArgsConstructor
public class CorrelationMDCInjectionFilter implements Filter {

    private static final String USER_KEY = "username";
    private static final String CORRELATION_ID = "correlationId";

    private final CorrelationId correlationId;

    @Override
    public void destroy() {
        // Destroy empty method
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        boolean successfulRegistration = false;
        HttpServletRequest req = (HttpServletRequest) request;
        Principal principal = req.getUserPrincipal();
        // Please note that we also could have used a cookie to
        // retrieve the user name

        if (principal != null) {
            successfulRegistration = extractPrincipalInfo(principal);
        }

        try {
            chain.doFilter(request, response);
        } finally {
            if (successfulRegistration) {
                MDC.remove(USER_KEY);
            }
            MDC.remove(CORRELATION_ID);
        }
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        // init empty method
    }

    public boolean extractPrincipalInfo(Principal principal) {
        MDC.put(CORRELATION_ID, correlationId.generate(null));

        return registerUsername(principal.getName());
    }

    /**
     * Register the user in the MDC under USER_KEY.
     *
     * @param username
     * @return true id the user can be successfully registered
     */
    private boolean registerUsername(String username) {
        if (StringUtils.isNotBlank(username)) {
            MDC.put(USER_KEY, username);
            return true;
        }

        return false;
    }
}
