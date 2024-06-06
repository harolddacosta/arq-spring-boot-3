/* AssentSoftware (C)2023 */
package com.decathlon.security.jwt.converters;

import org.springframework.security.oauth2.jwt.MappedJwtClaimSetConverter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DefaultClaimsConverter implements ClaimsConverter {

    private static final String CLAIM_ORGANIZATION = "organization";

    private final MappedJwtClaimSetConverter delegate =
            MappedJwtClaimSetConverter.withDefaults(Collections.emptyMap());

    @Override
    public Map<String, Object> convert(Map<String, Object> claims) {
        Map<String, Object> convertedClaims = this.delegate.convert(claims);

        if (convertedClaims == null) {
            convertedClaims = new HashMap<>();
        }

        String organization =
                convertedClaims.get(CLAIM_ORGANIZATION) != null
                        ? (String) convertedClaims.get(CLAIM_ORGANIZATION)
                        : "unknown";

        convertedClaims.put(CLAIM_ORGANIZATION, organization.toUpperCase());

        return convertedClaims;
    }
}
