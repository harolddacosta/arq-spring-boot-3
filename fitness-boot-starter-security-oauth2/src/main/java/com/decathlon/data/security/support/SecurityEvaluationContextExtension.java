/* Decathlon (C)2023 */
package com.decathlon.data.security.support;

import com.decathlon.security.SecurityConstants;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.data.spel.spi.EvaluationContextExtension;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class SecurityEvaluationContextExtension implements EvaluationContextExtension {

    @Override
    public String getExtensionId() {
        return "security";
    }

    @Override
    public SecurityExpressionRoot getRootObject() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return new SecurityExpressionRoot(authentication) {};
    }

    @Override
    public Map<String, Object> getProperties() {
        Map<String, Object> properties = new HashMap<>();

        SecurityExpressionRoot securityExpression = getRootObject();

        log.trace(
                "Type of:{} and value:{}",
                securityExpression.getPrincipal().getClass(),
                securityExpression.getPrincipal());

        properties.put(
                "countryCode",
                ((Jwt) securityExpression.getPrincipal())
                        .getClaim(SecurityConstants.CLAIM_COUNTRY_CODE));
        properties.put(
                "rootCenterId",
                ((Jwt) securityExpression.getPrincipal())
                        .getClaim(SecurityConstants.CLAIM_ROOT_CENTER_ID));
        try {
            log.trace(
                    "Value of {}:{}",
                    SecurityConstants.CLAIM_CENTERS_IDS,
                    ((Jwt) securityExpression.getPrincipal())
                            .getClaim(SecurityConstants.CLAIM_CENTERS_IDS));

            properties.put(
                    "centersId",
                    (((Jwt) securityExpression.getPrincipal())
                            .getClaim(SecurityConstants.CLAIM_CENTERS_IDS)));
        } catch (Exception e) {
            log.error(
                    "Problem getting centers ids from JWT claims:{}",
                    ExceptionUtils.getRootCause(e).getMessage());
        }

        return properties;
    }
}
