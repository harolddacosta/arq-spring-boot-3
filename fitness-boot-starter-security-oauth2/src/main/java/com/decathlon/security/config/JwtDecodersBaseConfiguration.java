/* AssentSoftware (C)2023 */
package com.decathlon.security.config;

import com.decathlon.security.jwt.converters.ClaimsConverter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.jose4j.keys.RsaKeyUtil;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.jwt.JwtClaimValidator;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;

@Slf4j
@RequiredArgsConstructor
public class JwtDecodersBaseConfiguration {

    private final OAuth2ResourceServerProperties[] properties;

    JwtDecoder certUriDecoder(ClaimsConverter claimsConverter) {
        OAuth2ResourceServerProperties currentPropertiesInstance = getPropertyInstance();

        NimbusJwtDecoder jwtDecoder =
                NimbusJwtDecoder.withJwkSetUri(currentPropertiesInstance.getJwt().getJwkSetUri())
                        .build();

        configureDecoder(jwtDecoder, claimsConverter);

        log.info("Configuring 'withJwkSetUri' NimbusJwtDecoder");

        return jwtDecoder;
    }

    JwtDecoder publicKeyDecoder(
            ClaimsConverter claimsConverter,
            @Value("${spring.security.oauth2.resourceserver.jwt.public-key-type:#{null}}")
                    String publicKeyType)
            throws Exception {
        NimbusJwtDecoder jwtDecoder = null;

        if (StringUtils.isNotBlank(publicKeyType) && "pem".equalsIgnoreCase(publicKeyType)) {
            jwtDecoder =
                    NimbusJwtDecoder.withPublicKey((RSAPublicKey) getPublicKeyInPemFormat())
                            .build();
        }

        configureDecoder(jwtDecoder, claimsConverter);

        log.info("Configuring 'withPublicKey' NimbusJwtDecoder");

        return jwtDecoder;
    }

    JwtDecoder secretKeyDecoder(
            ClaimsConverter claimsConverter,
            @Value("${spring.security.oauth2.resourceserver.jwt.secret-key}") String secretKey) {
        NimbusJwtDecoder jwtDecoder =
                NimbusJwtDecoder.withSecretKey(new SecretKeySpec(secretKey.getBytes(), "AES"))
                        .build();

        configureDecoder(jwtDecoder, claimsConverter);

        log.info("Configuring 'withSecretKey' NimbusJwtDecoder");

        return jwtDecoder;
    }

    private void configureDecoder(NimbusJwtDecoder jwtDecoder, ClaimsConverter claimsConverter) {
        OAuth2ResourceServerProperties currentPropertiesInstance = getPropertyInstance();

        List<OAuth2TokenValidator<Jwt>> validators = new ArrayList<>();
        validators.add(new JwtTimestampValidator());

        List<String> audiences = currentPropertiesInstance.getJwt().getAudiences();
        if (!CollectionUtils.isEmpty(audiences)) {
            validators.add(
                    new JwtClaimValidator<>(
                            JwtClaimNames.AUD,
                            aud ->
                                    aud != null
                                            && !Collections.disjoint(
                                                    (Collection<?>) aud, audiences)));
        }

        jwtDecoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(validators));
        jwtDecoder.setClaimSetConverter(claimsConverter);
    }

    private PublicKey getPublicKeyInPemFormat()
            throws InvalidKeySpecException, JoseException, IOException {
        OAuth2ResourceServerProperties currentPropertiesInstance = getPropertyInstance();

        RsaKeyUtil rsaKeyUtil = new RsaKeyUtil();

        return rsaKeyUtil.fromPemEncoded(currentPropertiesInstance.getJwt().readPublicKey());
    }

    private OAuth2ResourceServerProperties getPropertyInstance() {
        if (properties.length == 1) {
            return properties[0];
        }

        return List.of(properties).stream()
                .filter(
                        e ->
                                "org.springframework.boot.autoconfigure.security.oauth2.resource"
                                        .equalsIgnoreCase(e.getClass().getPackageName()))
                .findFirst()
                .orElseGet(OAuth2ResourceServerProperties::new);
    }
}
