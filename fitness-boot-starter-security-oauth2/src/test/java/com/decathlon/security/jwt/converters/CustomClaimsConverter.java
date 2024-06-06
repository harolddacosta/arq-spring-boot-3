/* AssentSoftware (C)2023 */
package com.decathlon.security.jwt.converters;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CustomClaimsConverter extends DefaultClaimsConverter {

    @Override
    public Map<String, Object> convert(Map<String, Object> claims) {
        return super.convert(claims);
    }
}
