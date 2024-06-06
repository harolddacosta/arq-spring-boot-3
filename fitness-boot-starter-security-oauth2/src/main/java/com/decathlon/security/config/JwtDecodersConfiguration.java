/* AssentSoftware (C)2023 */
package com.decathlon.security.config;

import com.decathlon.security.jwt.converters.ClaimsConverter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;

@Configuration
public class JwtDecodersConfiguration extends JwtDecodersBaseConfiguration {
    public JwtDecodersConfiguration(OAuth2ResourceServerProperties[] properties) {
        super(properties);
    }

    @Override
    @Bean("regularDecoder")
    @ConditionalOnProperty(
            prefix = "spring.security.oauth2.resourceserver",
            value = "jwt.decoder-type",
            havingValue = "jwk")
    JwtDecoder certUriDecoder(ClaimsConverter claimsConverter) {
        return super.certUriDecoder(claimsConverter);
    }

    @Override
    @Bean("regularDecoder")
    @ConditionalOnProperty(
            prefix = "spring.security.oauth2.resourceserver",
            value = "jwt.decoder-type",
            havingValue = "publicKey")
    JwtDecoder publicKeyDecoder(
            ClaimsConverter claimsConverter,
            @Value("${spring.security.oauth2.resourceserver.jwt.public-key-type:#{null}}")
                    String publicKeyType)
            throws Exception {
        return super.publicKeyDecoder(claimsConverter, publicKeyType);
    }

    @Override
    @Bean("regularDecoder")
    @ConditionalOnProperty(
            prefix = "spring.security.oauth2.resourceserver",
            value = "jwt.decoder-type",
            havingValue = "secretKey")
    JwtDecoder secretKeyDecoder(
            ClaimsConverter claimsConverter,
            @Value("${spring.security.oauth2.resourceserver.jwt.secret-key}") String secretKey) {
        return super.secretKeyDecoder(claimsConverter, secretKey);
    }
}
