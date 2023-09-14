/* Decathlon (C)2023 */
package com.decathlon.properties.exceptions;

public class PropertyDuplicatedException extends RuntimeException {

    private static final long serialVersionUID = 7216341141075440509L;

    public PropertyDuplicatedException(String propertyName) {
        super("The property '" + propertyName + "' is either present in storage or property file");
    }
}
