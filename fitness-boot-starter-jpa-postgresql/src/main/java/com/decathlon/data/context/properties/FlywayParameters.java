/* Decathlon (C)2024 */
package com.decathlon.data.context.properties;

import lombok.Getter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "spring")
@Getter
public class FlywayParameters {

    private final Map<String, String> flyway = new HashMap<>();
}
