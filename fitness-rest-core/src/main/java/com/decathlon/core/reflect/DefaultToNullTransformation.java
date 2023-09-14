/* Decathlon (C)2023 */
package com.decathlon.core.reflect;

import lombok.extern.slf4j.Slf4j;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.stream.Stream;

@Slf4j
public class DefaultToNullTransformation implements Transformation {

    private Object[] deleteCode;

    public DefaultToNullTransformation(Object... deleteCode) {
        super();

        this.deleteCode = deleteCode;
    }

    @Override
    public void convertValue(Serializable entity, Field field)
            throws IllegalArgumentException, IllegalAccessException {
        Object fieldValue = callGetter(entity, field.getName());
        log.trace("fieldValue:{} of field:{}", fieldValue, field.getName());

        Stream.of(deleteCode)
                .forEach(
                        (code) -> {
                            if (fieldValue != null && code.equals(fieldValue)) {
                                try {
                                    callSetter(entity, field.getName(), null);
                                } catch (IllegalArgumentException e) {
                                    // Do nothing
                                }
                            }
                        });
    }

    private Object callGetter(Object entity, String fieldName) {
        try {
            PropertyDescriptor propertyDescriptor =
                    new PropertyDescriptor(fieldName, entity.getClass());
            return propertyDescriptor.getReadMethod().invoke(entity);
        } catch (Exception e) {
            // Do nothing
        }

        return null;
    }

    private void callSetter(Object entity, String fieldName, Object newValue) {
        try {
            PropertyDescriptor propertyDescriptor =
                    new PropertyDescriptor(fieldName, entity.getClass());
            propertyDescriptor.getWriteMethod().invoke(entity, newValue);
        } catch (Exception e) {
            // Do nothing
        }
    }
}
