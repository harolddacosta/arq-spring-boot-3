/* AssentSoftware (C)2023 */
package com.decathlon.security;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@ComponentScan("com.decathlon.security")
@PropertySource("classpath:oauth.properties")
@EnableConfigurationProperties
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityOAuth2Configuration {}
