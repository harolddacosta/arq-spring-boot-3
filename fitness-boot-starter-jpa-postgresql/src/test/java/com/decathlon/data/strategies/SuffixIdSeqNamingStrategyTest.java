/* AssentSoftware (C)2023 */
package com.decathlon.data.strategies;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.relational.QualifiedName;
import org.hibernate.engine.jdbc.env.internal.NormalizingIdentifierHelperImpl;
import org.hibernate.engine.jdbc.env.spi.IdentifierHelper;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.hibernate.service.ServiceRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

@ExtendWith(MockitoExtension.class)
class SuffixIdSeqNamingStrategyTest {

    SuffixIdSeqNamingStrategy idSeqNamingStrategy = new SuffixIdSeqNamingStrategy();

    @Mock JdbcEnvironment jdbcEnvironmentImpl;
    @Mock ServiceRegistry serviceRegistry;

    @Spy
    IdentifierHelper identifierHelper =
            new NormalizingIdentifierHelperImpl(
                    jdbcEnvironmentImpl, null, false, false, false, false, null, null, null);

    @Test
    void when_determine_sequence_name() {
        when(serviceRegistry.getService(any())).thenReturn(jdbcEnvironmentImpl);
        when(jdbcEnvironmentImpl.getIdentifierHelper()).thenReturn(identifierHelper);

        QualifiedName qualifiedName =
                idSeqNamingStrategy.determineSequenceName(
                        Identifier.toIdentifier("public"),
                        Identifier.toIdentifier("public"),
                        Map.of("target_table", "persona"),
                        serviceRegistry);

        assertEquals(Identifier.toIdentifier("persona_id_seq"), qualifiedName.getObjectName());
    }

    @Test
    void when_determine_table_name() {
        when(serviceRegistry.getService(any())).thenReturn(jdbcEnvironmentImpl);
        when(jdbcEnvironmentImpl.getIdentifierHelper()).thenReturn(identifierHelper);
        when(identifierHelper.toIdentifier(null)).thenReturn(Identifier.toIdentifier("persona"));

        QualifiedName qualifiedName =
                idSeqNamingStrategy.determineTableName(
                        Identifier.toIdentifier("public"),
                        Identifier.toIdentifier("public"),
                        Map.of("target_table", "persona"),
                        serviceRegistry);

        assertEquals(Identifier.toIdentifier("persona"), qualifiedName.getObjectName());
    }
}
