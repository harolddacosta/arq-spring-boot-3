/* Decathlon (C)2023 */
package com.decathlon.properties.service;

public interface ExternalizedPropertiesService {

    String getConfigurationProperty(String propertyName);

    void createProperty(String propertyName, String propertyValue);

    void updatePropertyInConcurrentMode(String propertyName, String valueBefore, String valueAfter);

    void updateProperty(String propertyName, String propertyValue);
}
