/* Decathlon (C)2023 */
package com.decathlon.core.reflect;

import java.io.Serializable;
import java.lang.reflect.Field;

@FunctionalInterface
public interface Transformation {

    void convertValue(Serializable entity, Field field)
            throws IllegalArgumentException, IllegalAccessException;
}
