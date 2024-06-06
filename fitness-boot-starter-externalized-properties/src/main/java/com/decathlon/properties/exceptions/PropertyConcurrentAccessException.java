/* AssentSoftware (C)2023 */
package com.decathlon.properties.exceptions;

public class PropertyConcurrentAccessException extends RuntimeException {

    private static final long serialVersionUID = 491478942657553872L;

    public PropertyConcurrentAccessException(String dbField) {
        super("Concurrent access for property:" + dbField);
    }
}
