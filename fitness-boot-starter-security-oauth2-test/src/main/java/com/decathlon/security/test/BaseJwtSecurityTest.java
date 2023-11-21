/* Decathlon (C)2023 */
package com.decathlon.security.test;

import com.decathlon.security.test.web.servlet.request.BearerTokenRequestPostProcessor;

import org.springframework.beans.factory.annotation.Value;

class BaseJwtSecurityTest {

    @Value("${spring.security.oauth2.resourceserver.jwt.secret-key}")
    protected String secretKey;

    protected static BearerTokenRequestPostProcessor bearerToken(String token) {
        return new BearerTokenRequestPostProcessor(token);
    }
}
