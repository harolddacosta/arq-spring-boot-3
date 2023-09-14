/* Decathlon (C)2023 */
package com.decathlon.data.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class FlywayConfiguration {

    public FlywayConfiguration(
            DataSource dataSource,
            @Value("${spring.flyway.locations}") String flywayLocations,
            @Value("${spring.flyway.table}") String table,
            @Value("${spring.flyway.repair}") boolean repair) {
        Flyway flyway =
                Flyway.configure()
                        .baselineOnMigrate(true)
                        .dataSource(dataSource)
                        .locations(flywayLocations)
                        .outOfOrder(true)
                        .validateMigrationNaming(true)
                        .table(table)
                        .load();

        if (repair) {
            flyway.repair();
        }

        flyway.migrate();
    }
}
