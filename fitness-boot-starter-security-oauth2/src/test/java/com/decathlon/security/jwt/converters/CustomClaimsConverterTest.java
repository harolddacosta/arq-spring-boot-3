/* Decathlon (C)2023 */
package com.decathlon.security.jwt.converters;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

class CustomClaimsConverterTest {

    CustomClaimsConverter customClaimsConverter;

    @BeforeEach
    void setup() {
        customClaimsConverter = new CustomClaimsConverter();
    }

    @Test
    void when_contains() {
        Map<String, Object> authenticationToken =
                customClaimsConverter.convert(Map.of("iss", "oxylane"));

        assertThat(authenticationToken).contains(Map.entry("iss", "oxylane"));
    }

    @Test
    void when_contains_organization() {
        Map<String, Object> authenticationToken =
                customClaimsConverter.convert(Map.of("organization", "decathlon"));

        assertThat(authenticationToken).contains(Map.entry("organization", "DECATHLON"));
    }

    @Test
    void when_contains_unknown() {
        Map<String, Object> authenticationToken = customClaimsConverter.convert(Map.of());

        assertThat(authenticationToken).contains(Map.entry("organization", "UNKNOWN"));
    }

    @Test
    void when_contains_none() {
        Map<String, Object> authenticationToken = customClaimsConverter.convert(Map.of());

        assertThat(authenticationToken).contains(Map.entry("organization", "UNKNOWN"));
    }

    //    @Test
    //    void when_grant_roles_no_scopes() {
    //        String token =
    //
    // JwtBuilder.getInstance().secretKey("QzPuxfiQlsZyddSNQPjL8cr3mod4D89j").build();
    //
    //        Jwt tokenBuild =
    //                Jwt.withTokenValue(token)
    //                        .header("alg", JWSAlgorithm.HS256)
    //                        .claim("sub", "user")
    //                        .build();
    //
    //        Map<String, Object> authenticationToken = customClaimsConverter.convert(tokenBuild);
    //
    //        Collection<GrantedAuthority> authorities = authenticationToken.getAuthorities();
    //
    //        assertThat(authorities).isEmpty();
    //    }
}
