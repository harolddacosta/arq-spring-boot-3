/* Decathlon (C)2023 */
package com.decathlon.properties.jdbc.repositories.impl;

import com.decathlon.properties.exceptions.PropertyConcurrentAccessException;
import com.decathlon.properties.jdbc.repositories.PropertiesRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import javax.sql.DataSource;

@Repository
@ConditionalOnProperty(
        prefix = "app",
        value = "database.configurations-table.enabled",
        havingValue = "true",
        matchIfMissing = true)
@Slf4j
public class DatabasePropertiesRepository implements PropertiesRepository {

    private static final int PROPERTY_POSITION_INDEX = 1;

    private final JdbcTemplate jdbcTemplate;
    private final String configurationsSelect;
    private final String configurationsSelectForUpdate;
    private final String configurationsInsert;

    public DatabasePropertiesRepository(
            DataSource dataSource,
            @Value("${app.database.configurations-table-select}") String configurationsSelect,
            @Value("${app.database.configurations-table-select-for-update}")
                    String configurationsSelectForUpdate,
            @Value("${app.database.configurations-table-insert}") String configurationsInsert) {
        log.info(
                "DatabasePropertiesRepository initialized as the database.configurations-table.enabled is present and it's true");

        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.configurationsSelect = configurationsSelect;
        this.configurationsSelectForUpdate = configurationsSelectForUpdate;
        this.configurationsInsert = configurationsInsert;
    }

    @Override
    public String queryByField(String dbField) {
        return jdbcTemplate.queryForObject(configurationsSelect, String.class, dbField);
    }

    @Override
    public String queryClientDetailsOrganizacionByClientId(String clientId) {
        return jdbcTemplate.queryForObject(
                "select map_to_backend_id from oauth_client_details where client_id = ?",
                String.class,
                clientId);
    }

    @Override
    public void createFieldWithValue(String dbField, String value) {
        Object[] values = new Object[] {dbField, value};
        int[] types = new int[] {Types.VARCHAR, Types.VARCHAR};

        jdbcTemplate.update(configurationsInsert, values, types);
    }

    @Override
    public void updateFieldValueConcurrentlySafe(
            String dbField, String valueBefore, String valueAfter) {
        this.queryByField(dbField);
        String query = String.format("%s FOR UPDATE", configurationsSelectForUpdate);

        PreparedStatementCreatorFactory pscf =
                new PreparedStatementCreatorFactory(query, Types.VARCHAR);

        pscf.setUpdatableResults(true);
        RowCallbackHandler rch = callBackHandlerForConcurrentSave(dbField, valueBefore, valueAfter);

        jdbcTemplate.query(pscf.newPreparedStatementCreator(new Object[] {dbField}), rch);
    }

    @Override
    public void updateField(String dbField, String value) {
        this.queryByField(dbField);

        PreparedStatementCreatorFactory pscf =
                new PreparedStatementCreatorFactory(configurationsSelectForUpdate, Types.VARCHAR);

        pscf.setUpdatableResults(true);
        RowCallbackHandler rch = callBackHandlerForSimpleSave(value);

        jdbcTemplate.query(pscf.newPreparedStatementCreator(new Object[] {dbField}), rch);
    }

    private RowCallbackHandler callBackHandlerForConcurrentSave(
            String dbField, String valueBefore, String valueAfter) {
        return new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                if (resultSet.getString(PROPERTY_POSITION_INDEX).equalsIgnoreCase(valueBefore)) {
                    resultSet.updateString(PROPERTY_POSITION_INDEX, valueAfter);
                    resultSet.updateRow();
                } else {
                    throw new PropertyConcurrentAccessException(dbField);
                }
            }
        };
    }

    private RowCallbackHandler callBackHandlerForSimpleSave(String value) {
        return new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                resultSet.updateString(PROPERTY_POSITION_INDEX, value);
                resultSet.updateRow();
            }
        };
    }
}
