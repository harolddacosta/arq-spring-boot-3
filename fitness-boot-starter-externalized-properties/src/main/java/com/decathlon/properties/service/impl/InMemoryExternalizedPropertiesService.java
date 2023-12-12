/* Decathlon (C)2023 */
package com.decathlon.properties.service.impl;

import com.decathlon.properties.exceptions.PropertyConcurrentAccessException;
import com.decathlon.properties.exceptions.PropertyDuplicatedException;
import com.decathlon.properties.exceptions.PropertyNotFoundException;
import com.decathlon.properties.service.ExternalizedPropertiesService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Slf4j
public class InMemoryExternalizedPropertiesService implements ExternalizedPropertiesService {

    private final Environment env;

    private final Map<String, String> propertiesValues = new ConcurrentHashMap<>();

    @Override
    public String getConfigurationProperty(String propertyName) {
        if (StringUtils.isBlank(propertyName)) {
            throw new PropertyNotFoundException(propertyName);
        }

        String value = null;

        try {
            value = getValueFromMap(propertyName);
        } catch (PropertyNotFoundException propertyNotFoundException) {
            value = getValueFromPropertiesFile(propertyName);
        }

        return value;
    }

    @Override
    public void createProperty(String propertyName, String propertyValue) {
        try {
            if (StringUtils.isNotBlank(getConfigurationProperty(propertyName))) {
                throw new PropertyDuplicatedException(propertyName);
            }
        } catch (PropertyNotFoundException e) {
            validateAndPutProperty(propertyName, propertyValue);
        }
    }

    @Override
    public void updatePropertyInConcurrentMode(
            String propertyName, String valueBefore, String valueAfter) {
        String currentPropertyValue = getConfigurationProperty(propertyName);

        if (valueBefore.equalsIgnoreCase(currentPropertyValue)) {
            validateAndPutProperty(propertyName, valueAfter);
        } else {
            throw new PropertyConcurrentAccessException(propertyName);
        }
    }

    @Override
    public void updateProperty(String propertyName, String propertyValue) {
        getConfigurationProperty(propertyName);

        validateAndPutProperty(propertyName, propertyValue);
    }

    private void validateAndPutProperty(String propertyName, String propertyValue) {
        if (StringUtils.isNotBlank(propertyName)
                && (StringUtils.isNotBlank(propertyValue)
                        && !propertyValue.equalsIgnoreCase("null"))) {
            propertiesValues.put(propertyName, propertyValue);
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

    private String getValueFromMap(String propertyName) {
        if (!propertiesValues.containsKey(propertyName)) {
            log.warn(
                    "Properties - The configuration field '{}' is not present in Memory Map",
                    propertyName);

            throw new PropertyNotFoundException(propertyName);
        }

        return propertiesValues.get(propertyName);
    }
}
