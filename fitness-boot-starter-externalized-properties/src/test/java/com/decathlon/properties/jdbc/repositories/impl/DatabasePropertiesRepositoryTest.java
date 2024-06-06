/* AssentSoftware (C)2023 */
package com.decathlon.properties.jdbc.repositories.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.decathlon.properties.exceptions.PropertyConcurrentAccessException;
import com.decathlon.properties.jdbc.repositories.PropertiesRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;

@ComponentScan(basePackages = "com.decathlon.properties.jdbc.repositories")
@JdbcTest
class DatabasePropertiesRepositoryTest {

    @Autowired private PropertiesRepository databasePropertiesRepository;

    @Test
    void when_db_field_exists_with_right_value() {
        assertEquals(
                "assentsoftware@mail.com",
                databasePropertiesRepository.queryByField("mail_recipient"));
    }

    @Test
    void when_db_field_not_exists() {
        assertThrows(
                EmptyResultDataAccessException.class,
                () -> databasePropertiesRepository.queryByField("field_not_exists"));
    }

    @Test
    void select_from_oauth_details_table() {
        assertEquals(
                "10",
                databasePropertiesRepository.queryClientDetailsOrganizacionByClientId(
                        "publiservei"));
    }

    @Test
    void when_create_field_is_ok() {
        databasePropertiesRepository.createFieldWithValue("field_from_test", "value10");

        assertEquals("value10", databasePropertiesRepository.queryByField("field_from_test"));
    }

    @Test
    void when_update_dbfield_concurrently_is_ok() {
        databasePropertiesRepository.createFieldWithValue("field_concurrently_modified", "value1");
        databasePropertiesRepository.updateFieldValueConcurrentlySafe(
                "field_concurrently_modified", "value1", "value2");

        assertEquals(
                "value2", databasePropertiesRepository.queryByField("field_concurrently_modified"));
    }

    @Test
    void when_update_dbfield_concurrently_throws_exception() {
        databasePropertiesRepository.createFieldWithValue("field_concurrently_modified", "value1");

        assertThrows(
                PropertyConcurrentAccessException.class,
                () ->
                        databasePropertiesRepository.updateFieldValueConcurrentlySafe(
                                "field_concurrently_modified", "value3", "value2"));
    }

    @Test
    void when_update_dbfield_is_ok() {
        databasePropertiesRepository.createFieldWithValue("field_from_test", "value1");
        databasePropertiesRepository.updateField("field_from_test", "value2");

        assertEquals("value2", databasePropertiesRepository.queryByField("field_from_test"));
    }

    @Test
    void when_create_field_duplicated() {
        databasePropertiesRepository.createFieldWithValue("duplicated_field", "value1");

        assertThrows(
                DuplicateKeyException.class,
                () ->
                        databasePropertiesRepository.createFieldWithValue(
                                "duplicated_field", "value1"));
    }

    @Test
    void error_when_update_not_present_dbfield() {
        assertThrows(
                EmptyResultDataAccessException.class,
                () -> databasePropertiesRepository.updateField("not_present_field", "value2"));
    }

    @Test
    void error_when_update_concurrently_not_present_dbfield() {
        assertThrows(
                EmptyResultDataAccessException.class,
                () ->
                        databasePropertiesRepository.updateFieldValueConcurrentlySafe(
                                "not_present_field", "value2", "value1"));
    }
}
