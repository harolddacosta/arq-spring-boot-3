/* Decathlon (C)2023 */
package com.decathlon.data.config;

import com.decathlon.data.context.properties.FlywayParameters;

import org.apache.commons.text.CaseUtils;
import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class FlywayConfiguration {

    private static final String FLYWAY_ENABLED = "enabled";
    private static final String FLYWAY_REPAIR = "repair";

    private Set<String> ignoredProperties = Set.of(FLYWAY_ENABLED, FLYWAY_REPAIR);

    public FlywayConfiguration(FlywayParameters flywayParameters) {
        Map<String, String> flywayProperties =
                flywayParameters.getFlyway().entrySet().stream()
                        .filter(entry -> !ignoredProperties.contains(entry.getKey()))
                        .collect(
                                Collectors.toMap(
                                        entry ->
                                                "flyway."
                                                        + CaseUtils.toCamelCase(
                                                                entry.getKey(), false, '-'),
                                        Entry::getValue));

        Flyway flyway = Flyway.configure().configuration(flywayProperties).load();

        if (Boolean.TRUE
                .toString()
                .equalsIgnoreCase(flywayParameters.getFlyway().get(FLYWAY_REPAIR))) {
            flyway.repair();
        }

        flyway.migrate();
    }
}
