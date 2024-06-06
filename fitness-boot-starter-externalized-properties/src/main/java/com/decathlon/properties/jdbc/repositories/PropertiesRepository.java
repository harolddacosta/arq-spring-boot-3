/* AssentSoftware (C)2023 */
package com.decathlon.properties.jdbc.repositories;

public interface PropertiesRepository {

    String queryByField(String dbField);

    String queryClientDetailsOrganizacionByClientId(String clientId);

    void createFieldWithValue(String dbField, String value);

    void updateFieldValueConcurrentlySafe(String campo, String valueBefore, String valueAfter);

    void updateField(String campo, String value);
}
