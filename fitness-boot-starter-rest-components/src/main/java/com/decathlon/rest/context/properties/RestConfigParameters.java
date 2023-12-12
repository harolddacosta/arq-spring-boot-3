/* Decathlon (C)2023 */
package com.decathlon.rest.context.properties;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@ConfigurationProperties(prefix = "app")
@Data
public class RestConfigParameters implements Serializable {

    private static final long serialVersionUID = 695255049263841719L;

    private DatesProperties dates;
    private CorsProperties cors;
    private LocaleProperties locales;

    @Data
    public static class DatesProperties implements Serializable {

        private static final long serialVersionUID = -5331613327242365901L;

        private String dateFormat;
        private String dateTimeFormat;
    }

    @Data
    public static class CorsProperties implements Serializable {

        private static final long serialVersionUID = -4543679911579483522L;

        private String path;
        private String allowedOrigins;
        private String allowedHeaders;
        private String allowedMethods;
    }

    @Data
    public static class LocaleProperties implements Serializable {

        private static final long serialVersionUID = -4543679911579483522L;

        private String defaultLocale;
        private String[] supportedLocales;
    }
}
