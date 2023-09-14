/* Decathlon (C)2023 */
package com.decathlon.logging.audit.annotation;

import com.decathlon.logging.audit.annotation.Auditable.ActionResultEnum;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.Map;

public interface AuditableHandler {

    Map<String, String> extractFieldsFromTargetParameter(
            ProceedingJoinPoint point,
            String targetParameterName,
            String[] fieldsToExtractFromTargetParameter);

    AuditBuilderInfo extractFieldsFromCustomParameterBuilder(
            ProceedingJoinPoint point,
            String parameterBuilderBeanName,
            String parametersBuilderMethod,
            String[] parametersBuilderNames,
            Object methodProcessingResult);

    Map<String, String> buildPropertiesMap(
            String targetParametername,
            String[] fieldsToExtractFromTargetParameter,
            Object beanTargetForExtractFieldsToProcess);

    Auditable getAuditableAnnotationFromMethod(ProceedingJoinPoint point);

    MethodSignature extractMethodSignature(ProceedingJoinPoint point);

    void auditEvent(
            String eventLogCategory,
            String eventLogCode,
            ActionResultEnum result,
            Map<String, String> params,
            String description);
}
