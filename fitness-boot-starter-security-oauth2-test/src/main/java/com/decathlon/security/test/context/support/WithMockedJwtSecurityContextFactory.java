/* AssentSoftware (C)2023 */
package com.decathlon.security.test.context.support;

import com.decathlon.security.test.utils.JwtBuilder;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

@RequiredArgsConstructor
public class WithMockedJwtSecurityContextFactory
        implements WithSecurityContextFactory<WithMockedUser> {

    @NonNull private JwtDecoder jwtDecoder;

    @Value("${spring.security.oauth2.resourceserver.jwt.secret-key}")
    protected String secretKey;

    @Override
    public SecurityContext createSecurityContext(WithMockedUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        JwtBuilder jwtBuilder = JwtBuilder.getInstance().secretKey(secretKey);
        jwtBuilder.userName(customUser.userName());
        jwtBuilder.authorizeRoles(customUser.authorities());
        jwtBuilder.sub(customUser.userName());

        // Custom claims
        jwtBuilder.countryCode(customUser.countryCode());
        jwtBuilder.rootCenterId(Long.valueOf(customUser.rootCenterId().trim()));
        jwtBuilder.centersId(
                Arrays.stream(customUser.centersId())
                        .map(String::trim)
                        .map(Long::valueOf)
                        .toArray(Long[]::new));
        jwtBuilder.addCustomClaim(
                "organization_id", Long.valueOf(customUser.organizationId().trim()));
        jwtBuilder.addCustomClaim("entryUUID", UUID.randomUUID().toString());
        jwtBuilder.aud(customUser.aud());

        Jwt jwt = this.jwtDecoder.decode(jwtBuilder.build());
        Collection<GrantedAuthority> authorities =
                AuthorityUtils.createAuthorityList(customUser.authorities());
        JwtAuthenticationToken token = new JwtAuthenticationToken(jwt, authorities);

        context.setAuthentication(token);

        return context;
    }
}
