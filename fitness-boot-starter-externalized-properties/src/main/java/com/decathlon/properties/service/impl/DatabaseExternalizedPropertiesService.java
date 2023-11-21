/* Decathlon (C)2023 */
package com.decathlon.properties.service.impl;

import com.decathlon.properties.exceptions.PropertyConcurrentAccessException;
import com.decathlon.properties.exceptions.PropertyDuplicatedException;
import com.decathlon.properties.exceptions.PropertyNotFoundException;
import com.decathlon.properties.jdbc.repositories.PropertiesRepository;
import com.decathlon.properties.service.ExternalizedPropertiesService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
public class DatabaseExternalizedPropertiesService implements ExternalizedPropertiesService {

    private final Environment env;
    private final PropertiesRepository databaseConfigurationsRepository;

    @Override
    public String getConfigurationProperty(String propertyName) {
        if (StringUtils.isBlank(propertyName)) {
            throw new PropertyNotFoundException(propertyName);
        }

        String value = null;

        try {
            value = getValueFromDB(propertyName);
        } catch (PropertyNotFoundException propertyNotFoundException) {
            value = getValueFromPropertiesFile(propertyName);
        }

        return value;
    }

    @Override
    @Transactional(
            propagation = Propagation.REQUIRES_NEW,
            noRollbackFor = {PropertyNotFoundException.class})
    public void createProperty(String propertyName, String propertyValue) {
        try {
            if (StringUtils.isNotBlank(getConfigurationProperty(propertyName))) {
                throw new PropertyDuplicatedException(propertyName);
            }
        } catch (PropertyNotFoundException e) {
            validateAndCreateProperty(propertyName, propertyValue);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updatePropertyInConcurrentMode(
            String propertyName, String valueBefore, String valueAfter) {
        String currentPropertyValue = getConfigurationProperty(propertyName);

        if (valueBefore.equalsIgnoreCase(currentPropertyValue)) {
            databaseConfigurationsRepository.updateFieldValueConcurrentlySafe(
                    propertyName, valueBefore, valueAfter);
        } else {
            throw new PropertyConcurrentAccessException(propertyName);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateProperty(String propertyName, String propertyValue) {
        getConfigurationProperty(propertyName);

        databaseConfigurationsRepository.updateField(propertyName, propertyValue);
    }

    private void validateAndCreateProperty(String propertyName, String propertyValue) {
        if (StringUtils.isNotBlank(propertyName)
                && (StringUtils.isNotBlank(propertyValue)
                        && !propertyValue.equalsIgnoreCase("null"))) {
            databaseConfigurationsRepository.createFieldWithValue(propertyName, propertyValue);
        }
    }

    private String getValueFromPropertiesFile(String propertyName) {
        try {
            return env.getRequiredProperty(propertyName);
        } catch (IllegalStateException e) {
            log.warn(
                    "Properties - The configuration field '{}' is not present in properties file",
                    propertyName);

            throw new PropertyNotFoundException(propertyName);
        }
    }

    private String getValueFromDB(String propertyName) {
        try {
            return databaseConfigurationsRepository.queryByField(propertyName);
        } catch (EmptyResultDataAccessException e) {
            log.warn(
                    "Properties - The configuration field '{}' is not present in DB", propertyName);

            throw new PropertyNotFoundException(propertyName);
        }
    }
}
