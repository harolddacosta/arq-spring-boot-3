/* AssentSoftware (C)2023 */
package com.decathlon.security.jwt.utils;

import com.decathlon.security.SecurityConstants;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.List;

@Slf4j
public final class JwtUtils {

    private JwtUtils() {
        // Utility class
    }

    public static Jwt getJwtAuthenticatedUser() {
        return (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static JwtAuthenticationToken getJwtAuthenticationToken() {
        return (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
    }

    public static Long getRootCenterId() {
        return getJwtAuthenticatedUser().getClaim(SecurityConstants.CLAIM_ROOT_CENTER_ID);
    }

    public static String getCountryCode() {
        return getJwtAuthenticatedUser().getClaim(SecurityConstants.CLAIM_COUNTRY_CODE);
    }

    public static Long[] getCentersIds() {
        List<String> centersIds =
                getJwtAuthenticatedUser().getClaim(SecurityConstants.CLAIM_CENTERS_IDS);

        return centersIds.toArray(new Long[centersIds.size()]);
    }

    public static <T> T getCustomClaim(String claimKey) {
        return getJwtAuthenticatedUser().getClaim(claimKey);
    }

    public static boolean hasAccess(String role) {
        for (GrantedAuthority grant :
                SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
            if (grant.getAuthority().equalsIgnoreCase(role)) {
                log.trace("Comparing role '{}' with '{}'", grant.getAuthority(), role);

                return true;
            }
        }

        return false;
    }
}
