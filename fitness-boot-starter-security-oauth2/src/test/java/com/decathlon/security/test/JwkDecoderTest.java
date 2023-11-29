/* Decathlon (C)2023 */
package com.decathlon.security.test;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(
        properties = {
            "spring.security.oauth2.resourceserver.jwt.decoder-type=jwk",
            "spring.security.oauth2.resourceserver.jwt.jwk-set-uri=http://localhost:8787/auth/realms/dev/protocol/openid-connect/certs"
        })
class JwkDecoderTest {

    @Autowired JwtDecoder jwtDecoder;

    @Test
    void contextLoads(ApplicationContext context) {
        assertThat(context).isNotNull();
        assertThat(jwtDecoder).isNotNull();
    }
}
