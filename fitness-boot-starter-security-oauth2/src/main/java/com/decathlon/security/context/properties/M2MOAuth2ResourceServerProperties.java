/* AssentSoftware (C)2024 */
package com.decathlon.security.context.properties;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(
        prefix = "spring.security.oauth2.resourceserver",
        value = "m2m.enabled",
        havingValue = "true")
@Component("m2mOAuth2ResourceServerProperties")
@ConfigurationProperties(prefix = "spring.security.oauth2.resourceserver.m2m")
public class M2MOAuth2ResourceServerProperties extends OAuth2ResourceServerProperties {}
