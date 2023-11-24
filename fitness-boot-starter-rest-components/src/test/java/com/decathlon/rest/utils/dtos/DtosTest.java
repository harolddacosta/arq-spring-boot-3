/* Decathlon (C)2023 */
package com.decathlon.rest.utils.dtos;

import com.decathlon.rest.context.properties.RestConfigParameters;

import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanVerifier;

class DtosTest {

    @Test
    void test_KeyValueResponseDto_BeanVerifier() {
        BeanVerifier.forClass(KeyValueResponseDto.class)
                .editSettings()
                .setDefaultIterations(1)
                .edited()
                .verifyGettersAndSetters()
                .verifyEqualsAndHashCode()
                .verifyToString();
    }

    @Test
    void test_KeyValueXmlResponseDto_BeanVerifier() {
        BeanVerifier.forClass(KeyValueXmlResponseDto.class)
                .editSettings()
                .setDefaultIterations(1)
                .edited()
                .verifyGettersAndSetters()
                .verifyEqualsAndHashCode()
                .verifyToString();
    }

    @Test
    void test_RestConfigParameters_BeanVerifier() {
        BeanVerifier.forClass(RestConfigParameters.class)
                .editSettings()
                .setDefaultIterations(1)
                .edited()
                .verifyGettersAndSetters()
                .verifyEqualsAndHashCode()
                .verifyToString();
    }

    @Test
    void test_CorsProperties_BeanVerifier() {
        BeanVerifier.forClass(RestConfigParameters.CorsProperties.class)
                .editSettings()
                .setDefaultIterations(1)
                .edited()
                .verifyGettersAndSetters()
                .verifyEqualsAndHashCode()
                .verifyToString();
    }

    @Test
    void test_DatesProperties_BeanVerifier() {
        BeanVerifier.forClass(RestConfigParameters.DatesProperties.class)
                .editSettings()
                .setDefaultIterations(1)
                .edited()
                .verifyGettersAndSetters()
                .verifyEqualsAndHashCode()
                .verifyToString();
    }

    @Test
    void test_LocaleProperties_BeanVerifier() {
        BeanVerifier.forClass(RestConfigParameters.LocaleProperties.class)
                .editSettings()
                .setDefaultIterations(1)
                .edited()
                .verifyGettersAndSetters()
                .verifyEqualsAndHashCode()
                .verifyToString();
    }
}
