/* AssentSoftware (C)2023 */
package com.decathlon.logging.audit.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Auditable {

    enum ActionResultEnum {
        OK,
        KO
    }

    String[] fields() default {};

    String target() default "";

    String category() default "";

    String categoryWhenErrorOccurs() default "";

    String code() default "";

    String codeWhenErrorOccurs() default "";

    String message() default "";

    String messageWhenErrorOccurs() default "";

    String parametersBuilderFunctionName() default "";

    String[] parametersBuilderNames() default {};

    String parameterBuilderBeanName() default "";
}
