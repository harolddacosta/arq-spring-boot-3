/* Decathlon (C)2023 */
package com.decathlon.security.config;

import com.decathlon.security.jwt.converters.ClaimsConverter;
import com.decathlon.security.jwt.validators.AudienceValidator;

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
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;

@Slf4j
@Configuration
public class JwtDecodersConfiguration {

    @Bean
    @ConditionalOnProperty(
            prefix = "spring.security.oauth2.resourceserver",
            value = "jwt.decoder-type",
            havingValue = "jwk")
    JwtDecoder certUriDecoder(
            OAuth2ResourceServerProperties properties,
            @Value("${spring.security.oauth2.resourceserver.jwt.audience:#{null}}") String audience,
            ClaimsConverter claimsConverter) {
        NimbusJwtDecoder jwtDecoder =
                NimbusJwtDecoder.withJwkSetUri(properties.getJwt().getJwkSetUri()).build();

        configureDecoder(audience, claimsConverter, jwtDecoder);

        log.info("Configuring 'withJwkSetUri' NimbusJwtDecoder");

        return jwtDecoder;
    }

    @Bean
    @ConditionalOnProperty(
            prefix = "spring.security.oauth2.resourceserver",
            value = "jwt.decoder-type",
            havingValue = "publicKey")
    JwtDecoder publicKeyDecoder(
            OAuth2ResourceServerProperties properties,
            @Value("${spring.security.oauth2.resourceserver.jwt.audience:#{null}}") String audience,
            @Value("${spring.security.oauth2.resourceserver.jwt.public-key-type:#{null}}")
                    String publicKeyType,
            ClaimsConverter claimsConverter)
            throws Exception {
        NimbusJwtDecoder jwtDecoder = null;

        if (StringUtils.isNotBlank(publicKeyType) && "pem".equalsIgnoreCase(publicKeyType)) {
            jwtDecoder =
                    NimbusJwtDecoder.withPublicKey(
                                    (RSAPublicKey) getPublicKeyInPemFormat(properties))
                            .build();
        } else {
            jwtDecoder =
                    NimbusJwtDecoder.withPublicKey(
                                    (RSAPublicKey)
                                            getPublicKey(
                                                    properties.getJwt().getPublicKeyLocation()))
                            .build();
        }

        configureDecoder(audience, claimsConverter, jwtDecoder);

        log.info("Configuring 'withPublicKey' NimbusJwtDecoder");

        return jwtDecoder;
    }

    @Bean
    @ConditionalOnProperty(
            prefix = "spring.security.oauth2.resourceserver",
            value = "jwt.decoder-type",
            havingValue = "secretKey")
    JwtDecoder secretKeyDecoder(
            OAuth2ResourceServerProperties properties,
            @Value("${spring.security.oauth2.resourceserver.jwt.audience:#{null}}") String audience,
            @Value("${spring.security.oauth2.resourceserver.jwt.secret-key}") String secretKey,
            ClaimsConverter claimsConverter) {
        NimbusJwtDecoder jwtDecoder =
                NimbusJwtDecoder.withSecretKey(new SecretKeySpec(secretKey.getBytes(), "AES"))
                        .build();

        configureDecoder(audience, claimsConverter, jwtDecoder);

        log.info("Configuring 'withSecretKey' NimbusJwtDecoder");

        return jwtDecoder;
    }

    private void configureDecoder(
            String audience, ClaimsConverter claimsConverter, NimbusJwtDecoder jwtDecoder) {
        List<OAuth2TokenValidator<Jwt>> validators = new ArrayList<>();
        validators.add(new JwtTimestampValidator());

        if (StringUtils.isNotBlank(audience)) {
            validators.add(new AudienceValidator(audience));
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

    private PublicKey getPublicKeyInPemFormat(OAuth2ResourceServerProperties properties)
            throws InvalidKeySpecException, JoseException, IOException {
        RsaKeyUtil rsaKeyUtil = new RsaKeyUtil();

        return rsaKeyUtil.fromPemEncoded(properties.getJwt().readPublicKey());
    }
}
