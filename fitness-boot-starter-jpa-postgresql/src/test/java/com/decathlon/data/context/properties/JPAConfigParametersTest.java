/* Decathlon (C)2023 */
package com.decathlon.data.context.properties;

import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanVerifier;

class JPAConfigParametersTest {

    @Test
    void test_KeyValueResponseDto_BeanVerifier() {
        BeanVerifier.forClass(JPAConfigParameters.class)
                .editSettings()
                .setDefaultIterations(1)
                .edited()
                .verifyGettersAndSetters()
                .verifyToString();
    }
}
