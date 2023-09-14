/* Decathlon (C)2023 */
package com.decathlon.security.web.filter;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;

import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

@Slf4j
public class ApiKeyRequestFilterTest {

    @Mock
    private ApiKeyRequestFilter apiKeyRequestFilter =
            new ApiKeyRequestFilter("test-api-key", new ObjectMapper());

    @Test
    void when_filter_header_is_not_present() throws ServletException, IOException {
        MockHttpServletRequest req = Mockito.mock(MockHttpServletRequest.class);
        MockHttpServletResponse res = Mockito.mock(MockHttpServletResponse.class);
        MockFilterChain chain = Mockito.mock(MockFilterChain.class);

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(res.getWriter()).thenReturn(writer);

        apiKeyRequestFilter.doFilter(req, res, chain);

        log.debug("Exception:{}", stringWriter);

        Mockito.verifyNoInteractions(chain);

        SecurityContextHolder.clearContext();
    }

    @Test
    void when_filter_has_value() throws ServletException, IOException {
        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse res = Mockito.mock(MockHttpServletResponse.class);
        MockFilterChain chain = Mockito.mock(MockFilterChain.class);

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        req.addHeader("X-API-KEY", "test-api-key");

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(res.getWriter()).thenReturn(writer);

        apiKeyRequestFilter.doFilter(req, res, chain);

        verify(chain, times(1)).doFilter(req, res);

        SecurityContextHolder.clearContext();
    }

    @Test
    void when_filter_has_wrong_key() throws ServletException, IOException {
        MockHttpServletRequest req = Mockito.mock(MockHttpServletRequest.class);
        MockHttpServletResponse res = Mockito.mock(MockHttpServletResponse.class);
        MockFilterChain chain = Mockito.mock(MockFilterChain.class);

        apiKeyRequestFilter.doFilter(req, res, chain);

        Mockito.verifyNoInteractions(res);
    }
}
