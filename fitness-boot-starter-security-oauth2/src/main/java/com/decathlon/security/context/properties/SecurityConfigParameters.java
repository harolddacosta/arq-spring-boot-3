/* Decathlon (C)2023 */
package com.decathlon.security.context.properties;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@ConfigurationProperties(prefix = "app")
@Data
public class SecurityConfigParameters implements Serializable {

    private static final long serialVersionUID = 695255049263841719L;

    private CorsProperties cors;
    private JacksonProperties jackson;

    @Data
    public static class JacksonProperties implements Serializable {

        private static final long serialVersionUID = 5541880522575588387L;

        private boolean hibernateModuleEnable;
    }

    @Data
    public static class CorsProperties implements Serializable {

        private static final long serialVersionUID = -4543679911579483522L;

        private String path;
        private String allowedOrigins;
        private String allowedHeaders;
        private String allowedMethods;
    }
}
