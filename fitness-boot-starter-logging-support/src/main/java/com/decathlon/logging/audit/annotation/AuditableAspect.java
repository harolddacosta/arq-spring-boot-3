/* Decathlon (C)2023 */
package com.decathlon.logging.audit.annotation;

import com.decathlon.logging.audit.annotation.Auditable.ActionResultEnum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

@Component
@ConditionalOnProperty(
        prefix = "app",
        value = "logging.auditable.annotation.enabled",
        havingValue = "true",
        matchIfMissing = true)
@Aspect
@RequiredArgsConstructor
@Slf4j
public class AuditableAspect {

    private final AuditableHandler auditableHandler;

    private enum AnnotationProcessingTypeEnum {
        USE_TARGET,
        USE_CUSTOM_BUILDER,
        MIXED_VALUES_ERROR
    }

    @Around("execution(* *(..)) && @annotation(com.decathlon.logging.audit.annotation.Auditable)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object methodProcessingResult = null;

        Auditable auditableAnnotation = auditableHandler.getAuditableAnnotationFromMethod(point);

        AnnotationProcessingTypeEnum processingType =
                checkAnnotationValuesIntegrity(auditableAnnotation);

        if (processingType == AnnotationProcessingTypeEnum.USE_TARGET) {
            methodProcessingResult = doForTargetProcessing(point, auditableAnnotation);
        }

        if (processingType == AnnotationProcessingTypeEnum.USE_CUSTOM_BUILDER) {
            methodProcessingResult = doForCustomBuilderProcessing(point, auditableAnnotation);
        }

        if (processingType == AnnotationProcessingTypeEnum.MIXED_VALUES_ERROR) {
            log.error("Can't choose an audit strategy cause you are mixing annotation fields");

            methodProcessingResult = point.proceed();
        }

        return methodProcessingResult;
    }

    private Object doForTargetProcessing(ProceedingJoinPoint point, Auditable auditableAnnotation)
            throws Throwable {
        String eventLogCategory = auditableAnnotation.category();
        String eventLogCategoryWhenError = auditableAnnotation.categoryWhenErrorOccurs();
        String eventLogCode = auditableAnnotation.code();
        String eventLogCodeWhenError = auditableAnnotation.codeWhenErrorOccurs();
        String messageForAudit = auditableAnnotation.message();
        String messageForAuditWhenError = auditableAnnotation.messageWhenErrorOccurs();

        Map<String, String> propertiesMapForAudit =
                auditableHandler.extractFieldsFromTargetParameter(
                        point, auditableAnnotation.target(), auditableAnnotation.fields());

        Object methodProcessingResult = null;

        try {
            methodProcessingResult = point.proceed();

            auditableHandler.auditEvent(
                    eventLogCategory,
                    eventLogCode,
                    ActionResultEnum.OK,
                    propertiesMapForAudit,
                    messageForAudit);
        } catch (Throwable e) {
            auditableHandler.auditEvent(
                    StringUtils.isBlank(eventLogCategoryWhenError)
                            ? eventLogCategory
                            : eventLogCategoryWhenError,
                    StringUtils.isBlank(eventLogCodeWhenError)
                            ? eventLogCode
                            : eventLogCodeWhenError,
                    ActionResultEnum.KO,
                    propertiesMapForAudit,
                    StringUtils.isBlank(messageForAuditWhenError)
                            ? messageForAudit
                            : messageForAuditWhenError);

            throw e;
        }

        return methodProcessingResult;
    }

    private Object doForCustomBuilderProcessing(
            ProceedingJoinPoint point, Auditable auditableAnnotation) throws Throwable {
        Object methodProcessingResult = null;

        try {
            methodProcessingResult = point.proceed();

            AuditBuilderInfo auditBuilderInfo =
                    auditableHandler.extractFieldsFromCustomParameterBuilder(
                            point,
                            auditableAnnotation.parameterBuilderBeanName(),
                            auditableAnnotation.parametersBuilderFunctionName(),
                            auditableAnnotation.parametersBuilderNames(),
                            methodProcessingResult);

            auditableHandler.auditEvent(
                    auditBuilderInfo.getEventLogCategory(),
                    auditBuilderInfo.getEventLogCode(),
                    auditBuilderInfo.getResult(),
                    auditBuilderInfo.getParams(),
                    auditBuilderInfo.getDescription());
        } catch (Throwable e) {
            AuditBuilderInfo auditBuilderInfo =
                    auditableHandler.extractFieldsFromCustomParameterBuilder(
                            point,
                            auditableAnnotation.parameterBuilderBeanName(),
                            auditableAnnotation.parametersBuilderFunctionName(),
                            auditableAnnotation.parametersBuilderNames(),
                            e);

            auditableHandler.auditEvent(
                    auditBuilderInfo.getEventLogCategory(),
                    auditBuilderInfo.getEventLogCode(),
                    ActionResultEnum.KO,
                    auditBuilderInfo.getParams(),
                    auditBuilderInfo.getDescription());

            throw e;
        }

        return methodProcessingResult;
    }

    private AnnotationProcessingTypeEnum checkAnnotationValuesIntegrity(
            Auditable auditableAnnotation) {
        Map<String, Object> mapPropertiesForExternalBuilderProcessing = new HashMap<>();
        mapPropertiesForExternalBuilderProcessing.put(
                "parametersBuilderFunctionName",
                auditableAnnotation.parametersBuilderFunctionName());
        mapPropertiesForExternalBuilderProcessing.put(
                "parametersBuilderNames", auditableAnnotation.parametersBuilderNames());
        mapPropertiesForExternalBuilderProcessing.put(
                "parameterBuilderBeanName", auditableAnnotation.parameterBuilderBeanName());

        Map<String, Object> mapPropertiesForNotExternalBuilder = new HashMap<>();
        mapPropertiesForNotExternalBuilder.put("fields", auditableAnnotation.fields());
        mapPropertiesForNotExternalBuilder.put("target", auditableAnnotation.target());
        mapPropertiesForNotExternalBuilder.put("category", auditableAnnotation.category());
        mapPropertiesForNotExternalBuilder.put(
                "categoryWhenErrorOccurs", auditableAnnotation.categoryWhenErrorOccurs());
        mapPropertiesForNotExternalBuilder.put("code", auditableAnnotation.code());
        mapPropertiesForNotExternalBuilder.put(
                "codeWhenErrorOccurs", auditableAnnotation.codeWhenErrorOccurs());
        mapPropertiesForNotExternalBuilder.put("message", auditableAnnotation.message());
        mapPropertiesForNotExternalBuilder.put(
                "messageWhenErrorOccurs", auditableAnnotation.messageWhenErrorOccurs());

        boolean thereAreValuesForExternalBuilderProcessingValues =
                checkForValuesInProperties(mapPropertiesForExternalBuilderProcessing);
        boolean thereAreValuesForInternalBuilderProcessingValues =
                checkForValuesInProperties(mapPropertiesForNotExternalBuilder);

        if (thereAreValuesForExternalBuilderProcessingValues
                && thereAreValuesForInternalBuilderProcessingValues) {
            return AnnotationProcessingTypeEnum.MIXED_VALUES_ERROR;
        }

        if (thereAreValuesForExternalBuilderProcessingValues) {
            return AnnotationProcessingTypeEnum.USE_CUSTOM_BUILDER;
        }

        return AnnotationProcessingTypeEnum.USE_TARGET;
    }

    private boolean checkForValuesInProperties(Map<String, Object> annotationPropertiesToCheck) {
        Optional<Entry<String, Object>> property =
                annotationPropertiesToCheck.entrySet().stream().findFirst();

        return property.filter(this::hasValuesInProperties).isPresent();
    }

    private boolean hasValuesInProperties(Map.Entry<String, Object> mapElement) {
        if (mapElement.getValue() instanceof String[] values && values.length > 0) {
            return true;
        }

        return mapElement.getValue() instanceof String value && StringUtils.isNotBlank(value);
    }
}
