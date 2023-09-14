/* Decathlon (C)2023 */
package com.decathlon.data.strategies;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.relational.QualifiedName;
import org.hibernate.boot.model.relational.QualifiedNameParser;
import org.hibernate.boot.model.relational.QualifiedSequenceName;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.hibernate.id.enhanced.ImplicitDatabaseObjectNamingStrategy;
import org.hibernate.service.ServiceRegistry;

import java.util.Map;

public class SuffixIdSeqNamingStrategy implements ImplicitDatabaseObjectNamingStrategy {

    public static final String STRATEGY_NAME = "custom";
    private static final String DEF_TABLE = null;

    @Override
    public QualifiedName determineSequenceName(
            Identifier catalogName,
            Identifier schemaName,
            Map<?, ?> configValues,
            ServiceRegistry serviceRegistry) {
        final JdbcEnvironment jdbcEnvironment = serviceRegistry.getService(JdbcEnvironment.class);

        String seqName = ((String) configValues.get("target_table")).concat("_id_seq");

        return new QualifiedSequenceName(
                catalogName,
                schemaName,
                jdbcEnvironment.getIdentifierHelper().toIdentifier(seqName));
    }

    @Override
    public QualifiedName determineTableName(
            Identifier catalogName,
            Identifier schemaName,
            Map<?, ?> configValues,
            ServiceRegistry serviceRegistry) {
        final JdbcEnvironment jdbcEnvironment = serviceRegistry.getService(JdbcEnvironment.class);

        return new QualifiedNameParser.NameParts(
                catalogName,
                schemaName,
                jdbcEnvironment.getIdentifierHelper().toIdentifier(DEF_TABLE));
    }
}
