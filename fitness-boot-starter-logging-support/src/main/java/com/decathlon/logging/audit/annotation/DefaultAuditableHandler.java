/* Decathlon (C)2023 */
package com.decathlon.logging.audit.annotation;

import com.decathlon.logging.audit.EventsLogger;
import com.decathlon.logging.audit.annotation.Auditable.ActionResultEnum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
@ConditionalOnBean(AuditableAspect.class)
@RequiredArgsConstructor
@Slf4j
public class DefaultAuditableHandler implements AuditableHandler, ApplicationContextAware {

    private static final String UNKNOWN_CATEGORY = "Unknown category";
    private static final String UNKNOWN_CODE_AUDIT = "Unknown code audit";

    private ApplicationContext applicationContext;

    private final EventsLogger eventsLogger;

    @Override
    public Map<String, String> extractFieldsFromTargetParameter(
            ProceedingJoinPoint point,
            String targetParameterName,
            String[] fieldsToExtractFromTargetParameter) {
        Map<String, String> propertiesForAudit = new HashMap<>();
        boolean existsTargetParameter = false;
        Object targetParameterObjectForExtractFields = null;
        MethodSignature methodSignature = extractMethodSignature(point);

        for (int argPosition = 0;
                argPosition < methodSignature.getParameterNames().length && !existsTargetParameter;
                ++argPosition) {
            if (methodSignature.getParameterNames()[argPosition].equals(targetParameterName)) {
                existsTargetParameter = true;
                targetParameterObjectForExtractFields = point.getArgs()[argPosition];
            }
        }

        if (existsTargetParameter) {
            propertiesForAudit =
                    buildPropertiesMap(
                            targetParameterName,
                            fieldsToExtractFromTargetParameter,
                            targetParameterObjectForExtractFields);
        }

        return propertiesForAudit;
    }

    @Override
    public AuditBuilderInfo extractFieldsFromCustomParameterBuilder(
            ProceedingJoinPoint point,
            String parameterBuilderBeanName,
            String parametersBuilderMethod,
            String[] parametersBuilderNames,
            Object methodProcessingResult) {
        List<Object> parameterValues = new LinkedList<>();
        List<Class<?>> parameterTypes = new LinkedList<>();

        MethodSignature methodSignature = extractMethodSignature(point);

        extractParameters(
                point, parametersBuilderNames, methodSignature, parameterValues, parameterTypes);

        boolean resultIsException =
                checkMethodProcessingResult(
                        methodProcessingResult, parameterValues, parameterTypes, methodSignature);

        if (!parameterValues.isEmpty()) {
            try {
                Object beanForAuditParameterBuilder =
                        applicationContext.getBean(parameterBuilderBeanName);

                Method builderMethodToExecute =
                        MethodUtils.getAccessibleMethod(
                                beanForAuditParameterBuilder.getClass(),
                                parametersBuilderMethod,
                                parameterTypes.toArray(new Class[parameterTypes.size()]));

                return (AuditBuilderInfo)
                        builderMethodToExecute.invoke(
                                beanForAuditParameterBuilder,
                                parameterValues.toArray(new Object[parameterValues.size()]));
            } catch (BeansException e) {
                return AuditBuilderInfo.builder()
                        .eventLogCategory(UNKNOWN_CATEGORY)
                        .eventLogCode(UNKNOWN_CODE_AUDIT)
                        .result(resultIsException ? ActionResultEnum.KO : ActionResultEnum.OK)
                        .description(
                                "You defined a custom builder bean value that can't be obtained in spring context, did you created it?")
                        .build();
            } catch (Exception e) {
                log.error(
                        "There is a problem with the custom 'AuditBuilderInfo' implementation, maybe the bean exists but not the function or there is a NPE inside the builder",
                        e);

                return AuditBuilderInfo.builder()
                        .eventLogCategory(UNKNOWN_CATEGORY)
                        .eventLogCode(UNKNOWN_CODE_AUDIT)
                        .result(resultIsException ? ActionResultEnum.KO : ActionResultEnum.OK)
                        .description(
                                "Problem inside the custom params builder for audit the event: "
                                        + ExceptionUtils.getRootCauseMessage(e))
                        .build();
            }
        }

        return AuditBuilderInfo.builder()
                .eventLogCategory(UNKNOWN_CATEGORY)
                .eventLogCode(UNKNOWN_CODE_AUDIT)
                .result(resultIsException ? ActionResultEnum.KO : ActionResultEnum.OK)
                .description("You are auditing with CustomBuilder instead of simply using target")
                .build();
    }

    @Override
    public Map<String, String> buildPropertiesMap(
            String targetParameterName,
            String[] fieldsToExtractFromTargetParameter,
            final Object targetBeanForExtractFields) {
        if (fieldsToExtractFromTargetParameter == null
                || fieldsToExtractFromTargetParameter.length == 0) {
            return getSameTargetAsParameter(targetParameterName, targetBeanForExtractFields);
        }

        return getParametersFromTarget(
                fieldsToExtractFromTargetParameter, targetBeanForExtractFields);
    }

    @Override
    public Auditable getAuditableAnnotationFromMethod(ProceedingJoinPoint point) {
        return extractMethodSignature(point).getMethod().getAnnotation(Auditable.class);
    }

    @Override
    public MethodSignature extractMethodSignature(ProceedingJoinPoint point) {
        return ((MethodSignature) point.getSignature());
    }

    @Override
    public void auditEvent(
            String eventLogCategory,
            String eventLogCode,
            ActionResultEnum result,
            Map<String, String> params,
            String description) {
        try {
            eventsLogger.auditEvent(eventLogCategory, eventLogCode, result, description, params);
        } catch (Exception e) {
            log.error(
                    "Problem calling the event auditing function, it's not a bug related to the @auditable annotation",
                    e);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private Map<String, String> getParametersFromTarget(
            String[] fieldsToExtractFromTargetParameter, final Object targetBeanForExtractFields) {
        final Map<String, String> propertiesForAudit = new HashMap<>();
        Arrays.stream(fieldsToExtractFromTargetParameter)
                .forEach(
                        field -> {
                            try {
                                checkPropertyMapping(
                                        targetBeanForExtractFields, propertiesForAudit, field);
                            } catch (Exception e) {
                                log.error(
                                        "The field:{} can't be extracted from target bean for audit",
                                        field);
                            }
                        });

        return propertiesForAudit;
    }

    private Map<String, String> getSameTargetAsParameter(
            String targetParameterName, final Object targetBeanForExtractFields) {
        final Map<String, String> propertiesForAudit = new HashMap<>();

        try {
            propertiesForAudit.put(targetParameterName, targetBeanForExtractFields.toString());
        } catch (Exception e) {
            log.error(
                    "The field:{} can't be extracted from target bean for audit",
                    targetParameterName);
        }

        return propertiesForAudit;
    }

    private void checkPropertyMapping(
            final Object targetBeanForExtractFields,
            final Map<String, String> propertiesForAudit,
            String field)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (field.contains("::")) {
            String[] propertyMapped = StringUtils.split(field, "::");

            propertiesForAudit.put(
                    propertyMapped[0],
                    BeanUtils.getProperty(targetBeanForExtractFields, propertyMapped[1]));
        } else {
            propertiesForAudit.put(field, BeanUtils.getProperty(targetBeanForExtractFields, field));
        }
    }

    private boolean checkMethodProcessingResult(
            Object methodProcessingResult,
            List<Object> parameterValues,
            List<Class<?>> parameterTypes,
            MethodSignature methodSignature) {
        boolean resultIsException = false;

        if (methodProcessingResult != null) {
            parameterValues.add(methodProcessingResult);

            if (methodProcessingResult instanceof Throwable) {
                parameterTypes.add(Throwable.class);

                resultIsException = true;
            } else {
                parameterTypes.add(methodSignature.getReturnType());
            }
        }

        return resultIsException;
    }

    private void extractParameters(
            ProceedingJoinPoint point,
            String[] parametersBuilderNames,
            MethodSignature methodSignature,
            List<Object> parameterValues,
            List<Class<?>> parameterTypes) {
        for (int argPosition = 0;
                argPosition < methodSignature.getParameterNames().length;
                ++argPosition) {
            for (String parameterName : parametersBuilderNames) {
                if (methodSignature.getParameterNames()[argPosition].equals(parameterName)) {
                    parameterValues.add(point.getArgs()[argPosition]);
                    parameterTypes.add(methodSignature.getParameterTypes()[argPosition]);
                }
            }
        }
    }
}
