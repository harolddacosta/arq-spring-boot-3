/* Decathlon (C)2023 */
package com.decathlon.data.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public final class AuthenticatedUserAuditor implements AuditorAware<String> {

    private final String claimForAuditing;

    @Override
    public Optional<String> getCurrentAuditor() {
        log.trace(
                "Getting current authentication username for auditing task:{}",
                SecurityContextHolder.getContext().getAuthentication().getClass());

        if (SecurityContextHolder.getContext().getAuthentication()
                instanceof JwtAuthenticationToken jwtAuthenticationToken) {

            if (jwtAuthenticationToken.getTokenAttributes().containsKey(claimForAuditing)) {
                return Optional.of(
                        (String) jwtAuthenticationToken.getTokenAttributes().get(claimForAuditing));
            } else {
                log.info(
                        "The claim selected '{}' for auditing does not exists, fallback to name, please select a claim that effectively exists in the jwt",
                        claimForAuditing);
            }
        }

        return Optional.of(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}
