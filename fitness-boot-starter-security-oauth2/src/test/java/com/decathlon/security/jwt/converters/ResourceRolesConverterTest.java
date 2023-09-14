/* Decathlon (C)2023 */
package com.decathlon.security.jwt.converters;

import static org.assertj.core.api.Assertions.assertThat;

import com.decathlon.security.test.utils.JwtBuilder;
import com.nimbusds.jose.JWSAlgorithm;

import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j
class ResourceRolesConverterTest {

    ResourceRolesConverter resourceRolesConverter;

    @BeforeEach
    void setup() {
        resourceRolesConverter = new ResourceRolesConverter("decathlon");
    }

    @Test
    void when_grant_roles() {
        String token =
                JwtBuilder.getInstance()
                        .secretKey("QzPuxfiQlsZyddSNQPjL8cr3mod4D89j")
                        .scope(new String[] {"profile email openid"})
                        .build();

        Jwt tokenBuild =
                Jwt.withTokenValue(token)
                        .header("alg", JWSAlgorithm.HS256)
                        .claim("sub", "user")
                        .claim(
                                "resource_access",
                                Map.of(
                                        "decathlon",
                                        Map.of("roles", List.of("my_role", "other role"))))
                        .claim("scope", "profile email openid")
                        .build();

        AbstractAuthenticationToken authenticationToken =
                resourceRolesConverter.convert(tokenBuild);

        Collection<GrantedAuthority> authorities = authenticationToken.getAuthorities();

        assertThat(authorities)
                .containsExactlyInAnyOrder(
                        new SimpleGrantedAuthority("SCOPE_openid"),
                        new SimpleGrantedAuthority("SCOPE_email"),
                        new SimpleGrantedAuthority("SCOPE_profile"),
                        new SimpleGrantedAuthority("ROLE_MY_ROLE"),
                        new SimpleGrantedAuthority("ROLE_OTHER ROLE"));
    }

    @Test
    void when_grant_roles_no_scopes() {
        String token =
                JwtBuilder.getInstance()
                        .secretKey("QzPuxfiQlsZyddSNQPjL8cr3mod4D89j")
                        .scope(new String[] {"profile email openid"})
                        .build();

        Jwt tokenBuild =
                Jwt.withTokenValue(token)
                        .header("alg", JWSAlgorithm.HS256)
                        .claim("sub", "user")
                        .build();

        AbstractAuthenticationToken authenticationToken =
                resourceRolesConverter.convert(tokenBuild);

        Collection<GrantedAuthority> authorities = authenticationToken.getAuthorities();

        assertThat(authorities).isEmpty();
    }

    @Test
    void when_grant_roles_no_scopes_no_roles_no_resource() {
        String token =
                JwtBuilder.getInstance()
                        .secretKey("QzPuxfiQlsZyddSNQPjL8cr3mod4D89j")
                        .scope(new String[] {"profile email openid"})
                        .build();

        Jwt tokenBuild =
                Jwt.withTokenValue(token)
                        .header("alg", JWSAlgorithm.HS256)
                        .claim("sub", "user")
                        .claim("resource_access", Map.of("none", Map.of("roles", List.of())))
                        .build();

        AbstractAuthenticationToken authenticationToken =
                resourceRolesConverter.convert(tokenBuild);

        Collection<GrantedAuthority> authorities = authenticationToken.getAuthorities();

        assertThat(authorities).isEmpty();
    }

    @Test
    void when_grant_roles_no_scopes_no_roles() {
        String token =
                JwtBuilder.getInstance()
                        .secretKey("QzPuxfiQlsZyddSNQPjL8cr3mod4D89j")
                        .scope(new String[] {"profile email openid"})
                        .build();

        Jwt tokenBuild =
                Jwt.withTokenValue(token)
                        .header("alg", JWSAlgorithm.HS256)
                        .claim("sub", "user")
                        .claim("resource_access", Map.of("decathlon", Map.of("roles", List.of())))
                        .build();

        AbstractAuthenticationToken authenticationToken =
                resourceRolesConverter.convert(tokenBuild);

        Collection<GrantedAuthority> authorities = authenticationToken.getAuthorities();

        assertThat(authorities).isEmpty();
    }
}
