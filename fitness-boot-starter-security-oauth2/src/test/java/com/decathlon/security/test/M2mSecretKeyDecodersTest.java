/* AssentSoftware (C)2023 */
package com.decathlon.security.test;

import static org.assertj.core.api.Assertions.assertThat;

import com.decathlon.security.jwt.converters.AbstractRolesConverter;
import com.decathlon.security.jwt.converters.ResourceRolesConverter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(
        properties = {
            "spring.security.oauth2.resourceserver.m2m.enabled=true",
            "spring.security.oauth2.resourceserver.jwt.decoder-type=secretKey",
            "spring.security.oauth2.resourceserver.jwt.secret-key=QzPuxfiQlsZyddSNQPjL8cr3mod4D89j",
            "spring.security.oauth2.resourceserver.m2m.jwt.decoder-type=secretKey",
            "spring.security.oauth2.resourceserver.m2m.jwt.secret-key=QzPuxfiQlsZyddSNQPjL8cr3mod4D892",
            "spring.security.oauth2.resourceserver.m2m.jwt.audiences=valid-audience"
        })
class M2mSecretKeyDecodersTest {

    @Autowired JwtDecoder regularDecoder;
    @Autowired JwtDecoder m2mDecoder;

    @TestConfiguration
    public static class M2mDecodersTestConfiguration {
        @Bean
        AbstractRolesConverter rolesResolver(
                @Value("${spring.security.oauth2.resourceserver.jwt.resource-access-name}")
                        String resourceAccessName) {
            return new ResourceRolesConverter(resourceAccessName);
        }
    }

    @Test
    void contextLoads(ApplicationContext context) {
        assertThat(context).isNotNull();
        assertThat(regularDecoder).isNotNull();
        assertThat(m2mDecoder).isNotNull();

        NimbusJwtDecoder nimbusJwtDecoder = (NimbusJwtDecoder) regularDecoder;
        Jwt jwt =
                nimbusJwtDecoder.decode(
                        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJhdWQiOiJ2YWxpZC1hdWRpZW5jZSJ9.APocqtgLVt-fR_BjQ43QqzGei2Koum_MXisxBPozfcI");

        NimbusJwtDecoder m2mJwtDecoder = (NimbusJwtDecoder) m2mDecoder;
        Jwt jwtForM2mDecoder =
                m2mJwtDecoder.decode(
                        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJhdWQiOiJ2YWxpZC1hdWRpZW5jZSJ9.2wZ-laUotIsteqxXNsQxSTJWc2EXAA-gcJZtN0UBocY");

        assertThat(jwt.getClaimAsString("name")).isEqualTo("John Doe");
        assertThat(jwtForM2mDecoder.getClaimAsString("name")).isEqualTo("John Doe");
    }
}
