/* Decathlon (C)2023 */
package com.decathlon.security.jwt.converters;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractRolesConverter
        implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter defaultGrantedAuthoritiesConverter =
            new JwtGrantedAuthoritiesConverter();

    protected final String resourceId;

    public abstract Collection<SimpleGrantedAuthority> extractAdditionalRoles(final Jwt source);

    @Override
    public AbstractAuthenticationToken convert(final Jwt source) {
        Collection<GrantedAuthority> convertedRoles =
                defaultGrantedAuthoritiesConverter.convert(source);

        Collection<GrantedAuthority> authorities =
                Stream.concat(
                                convertedRoles != null && !convertedRoles.isEmpty()
                                        ? convertedRoles.stream()
                                        : Stream.empty(),
                                extractAdditionalRoles(source).stream())
                        .collect(Collectors.toSet());

        authorities =
                Stream.concat(
                                extractResourceRoles(source, resourceId).stream(),
                                authorities.stream())
                        .collect(Collectors.toSet());

        log.trace("Roles configured:{}", authorities);

        return new JwtAuthenticationToken(source, authorities);
    }

    @SuppressWarnings("unchecked")
    private static Collection<? extends GrantedAuthority> extractResourceRoles(
            final Jwt jwt, final String resourceId) {
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        Map<String, Object> resource;
        Collection<String> resourceRoles;

        if (resourceAccess != null
                && (resource = (Map<String, Object>) resourceAccess.get(resourceId)) != null
                && (resourceRoles = (Collection<String>) resource.get("roles")) != null) {
            return resourceRoles.stream()
                    .map(roleName -> new SimpleGrantedAuthority("ROLE_" + roleName.toUpperCase()))
                    .collect(Collectors.toSet());
        }

        return Collections.emptySet();
    }
}
