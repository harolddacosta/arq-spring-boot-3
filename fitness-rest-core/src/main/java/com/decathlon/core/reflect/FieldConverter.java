/* Decathlon (C)2023 */
package com.decathlon.core.reflect;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FieldConverter {

    private static final String DELETE_CODE_FOR_STRING = "@@DELETE";
    private static final Date DELETE_CODE_FOR_DATE = new Date(0);
    private static final LocalDate DELETE_CODE_FOR_LOCAL_DATE = LocalDate.of(1900, 01, 01);
    private static final Long DELETE_CODE_FOR_COMBOS_VALUE = -1L;
    private static final Float DELETE_CODE_FOR_FLOAT = Float.NaN;
    private static final Long DELETE_CODE_FOR_LONG = Long.MIN_VALUE;

    private Map<Class<?>, Transformation> codeTransformationProcess =
            Stream.of(
                            new AbstractMap.SimpleEntry<>(
                                    String.class,
                                    new DefaultToNullTransformation(DELETE_CODE_FOR_STRING)),
                            new AbstractMap.SimpleEntry<>(
                                    Date.class,
                                    new DefaultToNullTransformation(DELETE_CODE_FOR_DATE)),
                            new AbstractMap.SimpleEntry<>(
                                    LocalDate.class,
                                    new DefaultToNullTransformation(DELETE_CODE_FOR_LOCAL_DATE)),
                            new AbstractMap.SimpleEntry<>(
                                    Long.class,
                                    new DefaultToNullTransformation(
                                            DELETE_CODE_FOR_LONG, DELETE_CODE_FOR_COMBOS_VALUE)),
                            new AbstractMap.SimpleEntry<>(
                                    Float.class,
                                    new DefaultToNullTransformation(DELETE_CODE_FOR_FLOAT)))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    public static String getDeleteCodeForString() {
        return DELETE_CODE_FOR_STRING;
    }

    public static Float getDeleteCodeForFloat() {
        return DELETE_CODE_FOR_FLOAT;
    }

    public static Long getDeleteCodeForCombosValue() {
        return DELETE_CODE_FOR_COMBOS_VALUE;
    }

    public static Date getDeleteCodeForDate() {
        return DELETE_CODE_FOR_DATE;
    }

    public static LocalDate getDeleteCodeForLocalDate() {
        return DELETE_CODE_FOR_LOCAL_DATE;
    }

    public static Long getDeleteCodeForLong() {
        return DELETE_CODE_FOR_LONG;
    }

    public void addTransformationProcess(Class<?> clazz, Transformation process) {
        codeTransformationProcess.put(clazz, process);
    }

    public void replace(Serializable entity) {
        if (entity == null) {
            return;
        }

        Field[] fields = entity.getClass().getDeclaredFields();

        Stream.of(fields)
                .filter(
                        field ->
                                !Modifier.isFinal(field.getModifiers())
                                        && !Modifier.isStatic(field.getModifiers())
                                        && codeTransformationProcess.containsKey(field.getType()))
                .forEach(
                        field -> {
                            try {
                                codeTransformationProcess
                                        .get(field.getType())
                                        .convertValue(entity, field);
                            } catch (IllegalAccessException e) {
                                // Do nothing
                            }
                        });
    }
}
