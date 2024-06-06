/* AssentSoftware (C)2023 */
package com.decathlon.security.test;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(
        properties = {
            "spring.security.oauth2.resourceserver.jwt.decoder-type=secretKey",
            "spring.security.oauth2.resourceserver.jwt.secret-key=QzPuxfiQlsZyddSNQPjL8cr3mod4D89j",
            "spring.security.oauth2.resourceserver.jwt.audiences=valid-audience"
        })
class SecretKeyDecoderTest {

    @Autowired JwtDecoder jwtDecoder;

    @Test
    void contextLoads(ApplicationContext context) {
        assertThat(context).isNotNull();
        assertThat(jwtDecoder).isNotNull();

        NimbusJwtDecoder nimbusJwtDecoder = (NimbusJwtDecoder) jwtDecoder;
        Jwt jwt =
                nimbusJwtDecoder.decode(
                        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJhdWQiOiJ2YWxpZC1hdWRpZW5jZSJ9.APocqtgLVt-fR_BjQ43QqzGei2Koum_MXisxBPozfcI");

        assertThat(jwt.getClaimAsString("name")).isEqualTo("John Doe");
    }
}
