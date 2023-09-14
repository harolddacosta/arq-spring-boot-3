/* Decathlon (C)2023 */
package com.decathlon.properties.exceptions;

public class PropertyNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 7216341141075440509L;

    public PropertyNotFoundException(String propertyName) {
        super(
                "The property '"
                        + propertyName
                        + "' is not present either the persistence storage or property file");
    }
}
