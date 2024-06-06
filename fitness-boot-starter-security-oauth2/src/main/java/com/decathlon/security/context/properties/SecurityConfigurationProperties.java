/* AssentSoftware (C)2023 */
package com.decathlon.security.context.properties;

import lombok.Getter;
import lombok.Setter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@ConfigurationProperties(prefix = "app")
@Setter
@Getter
public class SecurityConfigurationProperties implements Serializable {

    private static final long serialVersionUID = 695255049263841719L;

    private CorsProperties cors;

    @Setter
    @Getter
    public static class CorsProperties implements Serializable {

        private static final long serialVersionUID = -4543679911579483522L;

        private String path;
        private String allowedOrigins;
        private String allowedHeaders;
        private String allowedMethods;
    }
}
