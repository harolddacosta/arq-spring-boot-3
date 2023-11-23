/* Decathlon (C)2023 */
package com.decathlon.core.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.decathlon.core.response.FieldErrorResource;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.List;

class LogicExceptionTest {

    @Test
    void test_with_constructor() {
        LogicException exception =
                new LogicException(
                        "Property not found",
                        "Please add the required Property",
                        "property.not.found",
                        null,
                        200);

        assertEquals("Property not found", exception.getTitle());
        assertEquals("Please add the required Property", exception.getDetail());
        assertEquals(400, exception.getStatus().getStatusCode());
        assertEquals("property.not.found", exception.getTranslationKey());
    }

    @Test
    void test_with_field_errors_constructor() {
        FieldErrorResource errorResource =
                new FieldErrorResource(
                        "Property not found", "lastName cant be empty", "lastName", "field.empty");

        LogicException exception =
                new LogicException(
                        "Property not found",
                        "Please add the required Property",
                        List.of(errorResource),
                        "property.not.found",
                        null,
                        200);

        assertEquals("Property not found", exception.getTitle());
        assertEquals("Please add the required Property", exception.getDetail());
        assertEquals(400, exception.getStatus().getStatusCode());
        assertEquals("property.not.found", exception.getTranslationKey());
        assertEquals(1, exception.getFieldErrors().size());
    }

    @Test
    void test_with_translationkeys_and_fielderrors() {
        LogicException exception =
                new LogicException(
                        "Property not found",
                        "Please add the required Property",
                        "property.not.found",
                        List.of("property.1", "property.2", "property.3"),
                        URI.create("www.google.com"),
                        2000);

        assertEquals("Property not found", exception.getTitle());
        assertEquals("Please add the required Property", exception.getDetail());
        assertEquals(400, exception.getStatus().getStatusCode());
        assertEquals("property.not.found", exception.getTranslationKey());
        assertEquals(URI.create("www.google.com"), exception.getUri());
        assertEquals(2000, exception.getCode());
    }

    @Test
    void test_with_translationkeys() {
        FieldErrorResource errorResource =
                new FieldErrorResource(
                        "Property not found", "lastName cant be empty", "lastName", "field.empty");

        LogicException exception =
                new LogicException(
                        "Property not found",
                        "Please add the required Property",
                        List.of(errorResource),
                        "property.not.found",
                        List.of("property.1", "property.2", "property.3"),
                        URI.create("www.google.com"),
                        2000);

        assertEquals("Property not found", exception.getTitle());
        assertEquals("Please add the required Property", exception.getDetail());
        assertEquals(400, exception.getStatus().getStatusCode());
        assertEquals("property.not.found", exception.getTranslationKey());
        assertEquals(URI.create("www.google.com"), exception.getUri());
        assertEquals(2000, exception.getCode());
    }
}
