/* AssentSoftware (C)2023 */
package com.decathlon.security.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.decathlon.security.authentication.M2MJwtAuthenticationManagerResolver;
import com.decathlon.security.jwt.converters.AbstractRolesConverter;
import com.decathlon.security.jwt.converters.ResourceRolesConverter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(
        properties = {
            "spring.security.oauth2.resourceserver.m2m.enabled=true",
            "spring.security.oauth2.resourceserver.m2m.claim-present-only=true",
            "spring.security.oauth2.resourceserver.m2m.claim-identifier=uuid",
            "spring.security.oauth2.resourceserver.jwt.decoder-type=secretKey",
            "spring.security.oauth2.resourceserver.jwt.secret-key=QzPuxfiQlsZyddSNQPjL8cr3mod4D89j",
            "spring.security.oauth2.resourceserver.m2m.jwt.decoder-type=secretKey",
            "spring.security.oauth2.resourceserver.m2m.jwt.secret-key=QzPuxfiQlsZyddSNQPjL8cr3mod4D892",
            "spring.security.oauth2.resourceserver.m2m.jwt.audiences=valid-audience"
        })
class M2mResolverCheckExistClaimTest {

    @Autowired JwtDecoder regularDecoder;
    @Autowired JwtDecoder m2mDecoder;
    @Autowired M2MJwtAuthenticationManagerResolver resolver;

    @TestConfiguration
    public static class M2mDecodersTestConfiguration {
        @Bean
        AbstractRolesConverter rolesResolver(
                @Value("${spring.security.oauth2.resourceserver.jwt.resource-access-name}")
                        String resourceAccessName) {
            return new ResourceRolesConverter(resourceAccessName);
        }
    }

    @Test
    void contextLoads(ApplicationContext context) {
        assertThat(context).isNotNull();
        assertThat(regularDecoder).isNotNull();
        assertThat(m2mDecoder).isNotNull();

        NimbusJwtDecoder nimbusJwtDecoder = (NimbusJwtDecoder) regularDecoder;
        Jwt jwt =
                nimbusJwtDecoder.decode(
                        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJhdWQiOiJ2YWxpZC1hdWRpZW5jZSIsInV1aWQiOiJpc19wcmVzZW50In0.j6WqXnVgQpjwzzd19SevvA0ehCa6aS3H-sY1ZeB-JIU");

        NimbusJwtDecoder m2mJwtDecoder = (NimbusJwtDecoder) m2mDecoder;
        Jwt jwtForM2mDecoder =
                m2mJwtDecoder.decode(
                        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJhdWQiOiJ2YWxpZC1hdWRpZW5jZSJ9.2wZ-laUotIsteqxXNsQxSTJWc2EXAA-gcJZtN0UBocY");

        assertThat(jwt.getClaimAsString("name")).isEqualTo("John Doe");
        assertThat(jwtForM2mDecoder.getClaimAsString("name")).isEqualTo("John Doe");
    }

    @Test
    void test_first_token() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("firstName", "Spring");
        request.setParameter("lastName", "Test");
        request.addHeader(
                "Authorization",
                "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJhdWQiOiJ2YWxpZC1hdWRpZW5jZSIsInV1aWQiOiJpc19wcmVzZW50In0.j6WqXnVgQpjwzzd19SevvA0ehCa6aS3H-sY1ZeB-JIU");

        AuthenticationManager resolverSelected = resolver.resolve(request);
        Authentication auth =
                resolverSelected.authenticate(
                        new BearerTokenAuthenticationToken(
                                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJhdWQiOiJ2YWxpZC1hdWRpZW5jZSIsInV1aWQiOiJpc19wcmVzZW50In0.j6WqXnVgQpjwzzd19SevvA0ehCa6aS3H-sY1ZeB-JIU"));

        assertNotNull(resolverSelected);
        assertNotNull(auth);
    }

    @Test
    void test_second_token() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("firstName", "Spring");
        request.setParameter("lastName", "Test");
        request.addHeader(
                "Authorization",
                "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJhdWQiOiJ2YWxpZC1hdWRpZW5jZSIsInV1aWQiOiJmMjUyMWYyOS0yMDIwLTRmMTEtYmJkMC1hZmVkOGZhZjI0NmIifQ.lY1dazGIuO2MqXaRx2_my9DVgb7Aa-fm7R_dcNGFBLo");

        AuthenticationManager resolverSelected = resolver.resolve(request);
        Authentication auth =
                resolverSelected.authenticate(
                        new BearerTokenAuthenticationToken(
                                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJhdWQiOiJ2YWxpZC1hdWRpZW5jZSIsInV1aWQiOiJmMjUyMWYyOS0yMDIwLTRmMTEtYmJkMC1hZmVkOGZhZjI0NmIifQ.lY1dazGIuO2MqXaRx2_my9DVgb7Aa-fm7R_dcNGFBLo"));

        assertNotNull(resolverSelected);
        assertNotNull(auth);
    }
}
