/* AssentSoftware (C)2023 */
package com.decathlon.security.test.context.support;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockedJwtSecurityContextFactory.class)
public @interface WithMockedUser {

    String userName() default "rachel";

    String[] authorities() default {"SCOPE_read"};

    String countryCode() default "ES";

    String rootCenterId() default "1";

    String[] centersId() default {"1", "2", "3"};

    String organizationId() default "2";

    String aud() default "account";
}
