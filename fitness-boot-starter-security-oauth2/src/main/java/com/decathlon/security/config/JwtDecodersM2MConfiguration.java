/* AssentSoftware (C)2023 */
package com.decathlon.security.config;

import com.decathlon.security.context.properties.M2MOAuth2ResourceServerProperties;
import com.decathlon.security.jwt.converters.ClaimsConverter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;

@ConditionalOnProperty(
        prefix = "spring.security.oauth2.resourceserver",
        value = "m2m.enabled",
        havingValue = "true")
@Configuration
public class JwtDecodersM2MConfiguration extends JwtDecodersBaseConfiguration {

    public JwtDecodersM2MConfiguration(M2MOAuth2ResourceServerProperties properties) {
        super(new OAuth2ResourceServerProperties[] {properties});
    }

    @Override
    @Bean("m2mDecoder")
    @ConditionalOnProperty(
            prefix = "spring.security.oauth2.resourceserver.m2m",
            value = "jwt.decoder-type",
            havingValue = "jwk")
    JwtDecoder certUriDecoder(ClaimsConverter claimsConverter) {
        return super.certUriDecoder(claimsConverter);
    }

    @Override
    @Bean("m2mDecoder")
    @ConditionalOnProperty(
            prefix = "spring.security.oauth2.resourceserver.m2m",
            value = "jwt.decoder-type",
            havingValue = "publicKey")
    JwtDecoder publicKeyDecoder(
            ClaimsConverter claimsConverter,
            @Value("${spring.security.oauth2.resourceserver.m2m.jwt.public-key-type:#{null}}")
                    String publicKeyType)
            throws Exception {
        return super.publicKeyDecoder(claimsConverter, publicKeyType);
    }

    @Override
    @Bean("m2mDecoder")
    @ConditionalOnProperty(
            prefix = "spring.security.oauth2.resourceserver.m2m",
            value = "jwt.decoder-type",
            havingValue = "secretKey")
    JwtDecoder secretKeyDecoder(
            ClaimsConverter claimsConverter,
            @Value("${spring.security.oauth2.resourceserver.m2m.jwt.secret-key}")
                    String secretKey) {
        return super.secretKeyDecoder(claimsConverter, secretKey);
    }
}
