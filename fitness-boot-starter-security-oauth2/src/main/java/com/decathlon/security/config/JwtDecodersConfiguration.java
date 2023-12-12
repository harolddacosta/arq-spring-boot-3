/* Decathlon (C)2023 */
package com.decathlon.security.config;

import com.decathlon.security.jwt.converters.ClaimsConverter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.jose4j.keys.RsaKeyUtil;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
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
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class JwtDecodersConfiguration {

    private final OAuth2ResourceServerProperties properties;

    @Bean
    @ConditionalOnProperty(
            prefix = "spring.security.oauth2.resourceserver",
            value = "jwt.decoder-type",
            havingValue = "jwk")
    JwtDecoder certUriDecoder(ClaimsConverter claimsConverter) {
        NimbusJwtDecoder jwtDecoder =
                NimbusJwtDecoder.withJwkSetUri(properties.getJwt().getJwkSetUri()).build();

        configureDecoder(jwtDecoder, claimsConverter);

        log.info("Configuring 'withJwkSetUri' NimbusJwtDecoder");

        return jwtDecoder;
    }

    @Bean
    @ConditionalOnProperty(
            prefix = "spring.security.oauth2.resourceserver",
            value = "jwt.decoder-type",
            havingValue = "publicKey")
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
        } else {
            jwtDecoder =
                    NimbusJwtDecoder.withPublicKey(
                                    (RSAPublicKey)
                                            getPublicKey(
                                                    properties.getJwt().getPublicKeyLocation()))
                            .build();
        }

        configureDecoder(jwtDecoder, claimsConverter);

        log.info("Configuring 'withPublicKey' NimbusJwtDecoder");

        return jwtDecoder;
    }

    @Bean
    @ConditionalOnProperty(
            prefix = "spring.security.oauth2.resourceserver",
            value = "jwt.decoder-type",
            havingValue = "secretKey")
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
        List<OAuth2TokenValidator<Jwt>> validators = new ArrayList<>();
        validators.add(new JwtTimestampValidator());

        List<String> audiences = properties.getJwt().getAudiences();
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

    private PublicKey getPublicKey(Resource resourceFile)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyContentAsBytes =
                Base64.getDecoder().decode(resourceFile.getInputStream().readAllBytes());

        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyContentAsBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");

        return kf.generatePublic(spec);
    }

    private PublicKey getPublicKeyInPemFormat()
            throws InvalidKeySpecException, JoseException, IOException {
        RsaKeyUtil rsaKeyUtil = new RsaKeyUtil();

        return rsaKeyUtil.fromPemEncoded(properties.getJwt().readPublicKey());
    }
}
