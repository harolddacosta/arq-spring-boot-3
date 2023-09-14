/* Decathlon (C)2021 */
package com.decathlon.core.response;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanVerifier;

class FieldErrorResourceTest {

    @Test
    void test_fieldErrorResource_BeanVerifier() {
        BeanVerifier.forClass(FieldErrorResource.class)
                .editSettings()
                .setDefaultIterations(1)
                .edited()
                .verifyGettersAndSetters()
                .verifyEqualsAndHashCode()
                .verifyToString();
    }

    @Test
    void test_fieldErrorResource_constructor() {
        FieldErrorResource errorResponse =
                new FieldErrorResource("lastName", "Not empty", "empty.true");

        assertNull(errorResponse.getIdentifier());
        assertNull(errorResponse.getCode());
        assertNull(errorResponse.getField());
        assertNotNull(errorResponse.getTitle());
        assertNotNull(errorResponse.getDetail());
        assertNotNull(errorResponse.getTranslationKey());
    }

    @Test
    void test_fieldErrorResource_constructor_parameters() {
        FieldErrorResource errorResponse =
                new FieldErrorResource(
                        "lastName", "Not empty", "empty.true", new String[] {"Param1", "param2"});

        assertNull(errorResponse.getIdentifier());
        assertNull(errorResponse.getCode());
        assertNull(errorResponse.getField());
        assertNotNull(errorResponse.getTitle());
        assertNotNull(errorResponse.getDetail());
        assertNotNull(errorResponse.getTranslationKey());
    }

    @Test
    void test_fieldErrorResource_constructor_key_parameters() {
        FieldErrorResource errorResponse =
                new FieldErrorResource(
                        "lastName",
                        "Not empty",
                        "field1",
                        "empty.true",
                        new String[] {"Param1", "param2"});

        assertNull(errorResponse.getIdentifier());
        assertNull(errorResponse.getCode());
        assertNotNull(errorResponse.getField());
        assertNotNull(errorResponse.getTitle());
        assertNotNull(errorResponse.getDetail());
        assertNotNull(errorResponse.getTranslationKey());
    }

    @Test
    void test_fieldErrorResource_constructor_field() {
        FieldErrorResource errorResponse =
                new FieldErrorResource("lastName", "Not empty", "field1", "empty.true");

        assertNull(errorResponse.getIdentifier());
        assertNull(errorResponse.getCode());
        assertNotNull(errorResponse.getField());
        assertNotNull(errorResponse.getTitle());
        assertNotNull(errorResponse.getDetail());
        assertNotNull(errorResponse.getTranslationKey());
    }

    @Test
    void test_fieldErrorResource_builder() {
        FieldErrorResource errorResponse =
                FieldErrorResource.builder()
                        .identifier(100L)
                        .code("code")
                        .field("field1")
                        .title("title1")
                        .detail("detail")
                        .translationKey("tk")
                        .translationKeyParameters(new String[] {"k1", "k2"})
                        .resource("resource")
                        .build();

        assertNotNull(errorResponse.getIdentifier());
        assertNotNull(errorResponse.getCode());
        assertNotNull(errorResponse.getField());
        assertNotNull(errorResponse.getTitle());
        assertNotNull(errorResponse.getDetail());
        assertNotNull(errorResponse.getTranslationKey());
    }
}
