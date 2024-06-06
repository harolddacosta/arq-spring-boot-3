/* AssentSoftware (C)2024 */
package com.decathlon.security.authentication;

import com.decathlon.security.jwt.converters.AbstractRolesConverter;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.oauth2.sdk.util.StringUtils;

import jakarta.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@ConditionalOnProperty(
        prefix = "spring.security.oauth2.resourceserver",
        value = "m2m.enabled",
        havingValue = "true")
@Component
public class M2MJwtAuthenticationManagerResolver
        implements AuthenticationManagerResolver<HttpServletRequest> {

    private final BearerTokenResolver resolver = new DefaultBearerTokenResolver();
    private final ConcurrentHashMap<String, AuthenticationManager> authenticationManagers =
            new ConcurrentHashMap<>();
    private final JwtDecoder m2mDecoder;
    private final JwtDecoder regularDecoder;
    private final String regularClientId;
    private final String m2mClientId;
    private final AbstractRolesConverter abstractRolesConverter;
    private final AbstractRolesConverter m2mRolesConverter;
    private final String m2mClaimIdentifier;
    private final boolean checkClaimPresentOnly;

    public M2MJwtAuthenticationManagerResolver(
            @Value("${spring.security.oauth2.resourceserver.client-id}") String regularClientId,
            @Value("${spring.security.oauth2.resourceserver.m2m.client-id}") String m2mClientId,
            @Value("${spring.security.oauth2.resourceserver.m2m.claim-identifier}")
                    String m2mClaimIdentifier,
            @Value("${spring.security.oauth2.resourceserver.m2m.claim-present-only}")
                    boolean checkClaimPresentOnly,
            JwtDecoder m2mDecoder,
            JwtDecoder regularDecoder,
            @Qualifier("rolesResolver") AbstractRolesConverter abstractRolesConverter,
            @Qualifier("m2mRolesResolver") AbstractRolesConverter m2mRolesConverter) {
        this.regularClientId = regularClientId;
        this.m2mClientId = m2mClientId;
        this.m2mDecoder = m2mDecoder;
        this.regularDecoder = regularDecoder;
        this.abstractRolesConverter = abstractRolesConverter;
        this.m2mRolesConverter = m2mRolesConverter;
        this.m2mClaimIdentifier = m2mClaimIdentifier;
        this.checkClaimPresentOnly = checkClaimPresentOnly;
    }

    @Override
    public AuthenticationManager resolve(HttpServletRequest context) {
        log.trace("Context:{}", this.resolver.resolve(context));

        return this.authenticationManagers.computeIfAbsent(toTenant(context), this::fromTenant);
    }

    private String toTenant(HttpServletRequest request) {
        String token = this.resolver.resolve(request);

        try {
            return (String) JWTParser.parse(token).getJWTClaimsSet().getClaim(m2mClaimIdentifier);
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    private AuthenticationManager fromTenant(String claim) {
        log.trace(
                "ClientId to search:{} regularClientId:{} m2mClientId:{}",
                claim,
                regularClientId,
                m2mClientId);

        return authentication -> {
            if (checkClaimPresentOnly) {
                if (StringUtils.isNotBlank(claim)) {
                    JwtAuthenticationProvider provider =
                            new JwtAuthenticationProvider(regularDecoder);
                    provider.setJwtAuthenticationConverter(abstractRolesConverter);

                    return provider.authenticate(authentication);
                } else {
                    JwtAuthenticationProvider provider = new JwtAuthenticationProvider(m2mDecoder);
                    provider.setJwtAuthenticationConverter(m2mRolesConverter);

                    return provider.authenticate(authentication);
                }
            } else {
                if (claim.equals(regularClientId)) {
                    JwtAuthenticationProvider provider =
                            new JwtAuthenticationProvider(regularDecoder);
                    provider.setJwtAuthenticationConverter(abstractRolesConverter);

                    return provider.authenticate(authentication);
                } else if (claim.equals(m2mClientId)) {
                    JwtAuthenticationProvider provider = new JwtAuthenticationProvider(m2mDecoder);
                    provider.setJwtAuthenticationConverter(m2mRolesConverter);

                    return provider.authenticate(authentication);
                }
            }

            throw new IllegalArgumentException("Unknown tenant");
        };
    }
}
