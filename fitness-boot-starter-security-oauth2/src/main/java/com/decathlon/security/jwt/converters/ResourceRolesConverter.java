/* Decathlon (C)2023 */
package com.decathlon.security.jwt.converters;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.Collections;

public class ResourceRolesConverter extends AbstractRolesConverter {

    public ResourceRolesConverter(String resourceId) {
        super(resourceId);
    }

    @Override
    public Collection<SimpleGrantedAuthority> extractAdditionalRoles(Jwt source) {
        return Collections.emptyList();
    }
}
