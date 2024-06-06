/* AssentSoftware (C)2023 */
package com.decathlon.properties.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.decathlon.properties.exceptions.PropertyConcurrentAccessException;
import com.decathlon.properties.exceptions.PropertyDuplicatedException;
import com.decathlon.properties.exceptions.PropertyNotFoundException;
import com.decathlon.properties.service.ExternalizedPropertiesService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@TestPropertySource("classpath:application.properties")
class InMemoryExternalizedPropertiesServiceTest {

    @TestConfiguration
    static class EmployeeServiceImplTestContextConfiguration {

        @Bean
        ExternalizedPropertiesService externalizedPropertiesService(Environment env) {
            return new InMemoryExternalizedPropertiesService(env);
        }
    }

    @Autowired private ExternalizedPropertiesService externalizedPropertiesService;

    @Test
    void ok_when_property_exists_in_map() {
        externalizedPropertiesService.createProperty("property_saved_in_map", "value1");

        assertEquals(
                "value1",
                externalizedPropertiesService.getConfigurationProperty("property_saved_in_map"));
    }

    @Test
    void ok_when_property_exists_in_configuration_file() {
        assertEquals(
                "Hi, I'm a property from configuration file",
                externalizedPropertiesService.getConfigurationProperty("from.file"));
    }

    @Test
    void error_when_property_does_not_exist_any_place() {
        assertThrows(
                PropertyNotFoundException.class,
                () ->
                        externalizedPropertiesService.getConfigurationProperty(
                                "property_not_present"));
    }

    @Test
    void ok_when_property_is_duplicated() {
        externalizedPropertiesService.createProperty("property_duplicated", "value1");
        assertThrows(
                PropertyDuplicatedException.class,
                () ->
                        externalizedPropertiesService.createProperty(
                                "property_duplicated", "value1"));
    }

    @Test
    void error_when_property_value_is_empty() {
        externalizedPropertiesService.createProperty("property_with_blank_value", null);
        assertThrows(
                PropertyNotFoundException.class,
                () ->
                        externalizedPropertiesService.getConfigurationProperty(
                                "property_with_blank_value"));

        externalizedPropertiesService.createProperty("property_with_blank_value", "");
        assertThrows(
                PropertyNotFoundException.class,
                () ->
                        externalizedPropertiesService.getConfigurationProperty(
                                "property_with_blank_value"));

        externalizedPropertiesService.createProperty("property_with_blank_value", "   ");
        assertThrows(
                PropertyNotFoundException.class,
                () ->
                        externalizedPropertiesService.getConfigurationProperty(
                                "property_with_blank_value"));

        externalizedPropertiesService.createProperty("property_with_blank_value", "null");
        assertThrows(
                PropertyNotFoundException.class,
                () ->
                        externalizedPropertiesService.getConfigurationProperty(
                                "property_with_blank_value"));
    }

    @Test
    void error_when_update_not_present_property() {
        assertThrows(
                PropertyNotFoundException.class,
                () ->
                        externalizedPropertiesService.updateProperty(
                                "update_property_not_present", "valueBefore"));
    }

    @Test
    void error_when_update_in_concurrent_mode_not_present_property() {
        assertThrows(
                PropertyNotFoundException.class,
                () ->
                        externalizedPropertiesService.updatePropertyInConcurrentMode(
                                "concurrent_property_not_present", "valueBefore", "valueAfter"));
    }

    @Test
    void ok_when_create_and_update_property_in_map() {
        externalizedPropertiesService.createProperty(
                "property_created_and_updated_in_map", "value1");
        externalizedPropertiesService.updateProperty(
                "property_created_and_updated_in_map", "value10");

        assertEquals(
                "value10",
                externalizedPropertiesService.getConfigurationProperty(
                        "property_created_and_updated_in_map"));
    }

    @Test
    void ok_when_create_and_update_property_concurrently_in_map() {
        externalizedPropertiesService.createProperty(
                "property_created_concurrently_and_updated_in_map", "value1");
        externalizedPropertiesService.updatePropertyInConcurrentMode(
                "property_created_concurrently_and_updated_in_map", "value1", "value10");

        assertEquals(
                "value10",
                externalizedPropertiesService.getConfigurationProperty(
                        "property_created_concurrently_and_updated_in_map"));
    }

    @Test
    void concurrent_error_when_create_and_update_property_concurrently_in_map() {
        externalizedPropertiesService.createProperty(
                "property_failed_created_concurrently_and_updated_in_map", "value1");

        assertThrows(
                PropertyConcurrentAccessException.class,
                () ->
                        externalizedPropertiesService.updatePropertyInConcurrentMode(
                                "property_failed_created_concurrently_and_updated_in_map",
                                "value20",
                                "value10"));
    }

    @Test
    void error_on_get_configuration_property_null_args() {
        assertThrows(
                PropertyNotFoundException.class,
                () -> externalizedPropertiesService.getConfigurationProperty(null));
    }

    @Test
    void error_on_create_property_null_args() {
        assertThrows(
                PropertyNotFoundException.class,
                () -> externalizedPropertiesService.updateProperty(null, null));

        assertThrows(
                PropertyNotFoundException.class,
                () ->
                        externalizedPropertiesService.updateProperty(
                                "property_with_null_value", null));

        assertThrows(
                PropertyNotFoundException.class,
                () -> externalizedPropertiesService.updateProperty(null, "value for null key"));
    }
}
