/* AssentSoftware (C)2024 */
package com.decathlon.security.jwt.converters;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component("m2mRolesResolver")
public class M2MRolesConverter extends AbstractRolesConverter {

    public M2MRolesConverter(
            @Value("${spring.security.oauth2.resourceserver.jwt.resource-access-name}")
                    final String resourceId) {
        super(resourceId);
    }

    @Override
    public Collection<SimpleGrantedAuthority> extractAdditionalRoles(Jwt source) {
        return List.of(new SimpleGrantedAuthority("ROLE_M2M"));
    }
}
