/* Decathlon (C)2023 */
package com.decathlon.core.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ResourceNotFoundExceptionTest {

    @Test
    void test_with_constructor() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Resource Not Found");

        assertEquals("Resource Not Found", exception.getMessage());
    }

    @Test
    void test_no_constructor() {
        ResourceNotFoundException exception = new ResourceNotFoundException();

        assertEquals("EntityRepresentationModel not found!", exception.getMessage());
    }
}
