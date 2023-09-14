/* Decathlon (C)2023 */
package com.decathlon.security.test.web.servlet.request;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

public class BearerTokenRequestPostProcessor implements RequestPostProcessor {

    private String token;

    public BearerTokenRequestPostProcessor(String token) {
        this.token = token;
    }

    @Override
    public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
        request.addHeader("Authorization", "Bearer " + this.token);

        return request;
    }
}
