/* Decathlon (C)2023 */
package com.decathlon.core.reflect;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.decathlon.core.reflect.bean.ReflectionBean;
import com.decathlon.core.reflect.bean.ReflectionNoAttributesBean;

import org.junit.jupiter.api.Test;

class FieldConverterTest {

    @Test
    void check_null_for_string() {
        ReflectionBean reflectionBean =
                ReflectionBean.builder()
                        .stringValue(FieldConverter.getDeleteCodeForString())
                        .dateValue(FieldConverter.getDeleteCodeForDate())
                        .localDateValue(FieldConverter.getDeleteCodeForLocalDate())
                        .longValue(FieldConverter.getDeleteCodeForCombosValue())
                        .floatValue(FieldConverter.getDeleteCodeForFloat())
                        .build();

        FieldConverter fieldConverter = new FieldConverter();
        fieldConverter.replace(reflectionBean);

        assertNull(reflectionBean.getStringValue());
        assertNull(reflectionBean.getDateValue());
        assertNull(reflectionBean.getLocalDateValue());
        assertNull(reflectionBean.getLongValue());
        assertNull(reflectionBean.getFloatValue());
    }

    @Test
    void check_null_for_long() {
        ReflectionBean reflectionBean =
                ReflectionBean.builder().longValue(FieldConverter.getDeleteCodeForLong()).build();

        FieldConverter fieldConverter = new FieldConverter();
        fieldConverter.replace(reflectionBean);

        assertNull(reflectionBean.getLongValue());
    }

    @Test
    void check_null_for_byte() {
        ReflectionBean reflectionBean =
                ReflectionBean.builder().stringValue("Prueba sin el cambio").build();

        assertEquals("Prueba sin el cambio", reflectionBean.getStringValue());

        FieldConverter fieldConverter = new FieldConverter();
        fieldConverter.addTransformationProcess(
                String.class,
                (entity, field) -> {
                    ((ReflectionBean) entity).setStringValue("Prueba de cambio");
                });

        fieldConverter.replace(reflectionBean);

        assertEquals("Prueba de cambio", reflectionBean.getStringValue());
    }

    @Test
    void check_no_attributes() {
        ReflectionNoAttributesBean reflectionBean = new ReflectionNoAttributesBean();

        FieldConverter fieldConverter = new FieldConverter();
        fieldConverter.replace(reflectionBean);

        assertNotNull(reflectionBean);
    }

    @Test
    void check_null() {
        FieldConverter fieldConverter = new FieldConverter();
        fieldConverter.replace(null);

        assertTrue(true);
    }
}
