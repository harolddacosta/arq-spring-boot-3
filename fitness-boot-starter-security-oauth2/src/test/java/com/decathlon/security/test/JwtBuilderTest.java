/* Decathlon (C)2023 */
package com.decathlon.security.test;

import com.decathlon.security.test.utils.JwtBuilder;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

class JwtBuilderTest {

    @Test
    void builderTest() {
        JwtBuilder jwtBuilder =
                JwtBuilder.getInstance().secretKey("QzPuxfiQlsZyddSNQPjL8cr3mod4D89j");
        jwtBuilder.userName("Harold");
        jwtBuilder.authorizeRoles(new String[] {"Scope_read"});

        // Custom claims
        jwtBuilder.countryCode("ES");
        jwtBuilder.rootCenterId(Long.valueOf("1"));
        jwtBuilder.centersId(
                Arrays.stream(new String[] {"1", "2", "3"})
                        .map(String::trim)
                        .map(Long::valueOf)
                        .toArray(Long[]::new));

        jwtBuilder.build();
    }
}
