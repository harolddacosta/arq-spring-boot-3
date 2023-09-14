/* Decathlon (C)2023 */
package com.decathlon.logging.audit;

import com.decathlon.logging.audit.annotation.AuditBuilderInfo;
import com.decathlon.logging.audit.annotation.Auditable.ActionResultEnum;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class AuditableParameterBuilder {

    public AuditBuilderInfo buildParametersForAnnotation(
            AuditableParamBean auditableParamBean, String otherParam) {
        log.info("Calling a external buildParameters");

        Map<String, String> params = new HashMap<>();
        params.put("CodigoPostalCharallave", auditableParamBean.getAddress().getPostalCode());

        AuditBuilderInfo auditBuilderInfo =
                AuditBuilderInfo.builder()
                        .params(params)
                        .description("This description was generated from the builder")
                        .eventLogCategory("Category set from the builder")
                        .eventLogCode("Log set from the builder")
                        .result(ActionResultEnum.OK)
                        .build();

        return auditBuilderInfo;
    }

    public AuditBuilderInfo buildParametersForAnnotation(
            AuditableParamBean auditableParamBean, String otherParam, Throwable e) {
        log.info("Calling a external buildParameters with Exception");

        Map<String, String> params = new HashMap<>();
        params.put("CodigoPostalCharallave", auditableParamBean.getAddress().getPostalCode());

        AuditBuilderInfo auditBuilderInfo =
                AuditBuilderInfo.builder()
                        .params(params)
                        .description(
                                "This description was generated from the builder when exception occurs")
                        .eventLogCategory("Category set from the builder when exception")
                        .eventLogCode("Log set from the builder when exception")
                        .result(ActionResultEnum.OK)
                        .build();

        return auditBuilderInfo;
    }

    public AuditBuilderInfo buildParametersForAnnotationWithReturnValue(
            AuditableParamBean auditableParamBean,
            String otherParam,
            ResponseEntity<AuditableParamBean> result) {
        log.info("Calling a external buildParameters with return value");

        Map<String, String> params = new HashMap<>();
        params.put("CodigoPostalCharallave", auditableParamBean.getAddress().getPostalCode());

        AuditBuilderInfo auditBuilderInfo =
                AuditBuilderInfo.builder()
                        .params(params)
                        .description(result.getBody().getName())
                        .eventLogCategory("Category set from the builder")
                        .eventLogCode("Log set from the builder")
                        .result(ActionResultEnum.OK)
                        .build();

        return auditBuilderInfo;
    }

    public AuditBuilderInfo buildParametersForAnnotationWithReturnValue(
            AuditableParamBean auditableParamBean, String otherParam, Throwable e) {
        log.info("Calling a external buildParameters with return value and exception");

        Map<String, String> params = new HashMap<>();
        params.put("CodigoPostalCharallave", auditableParamBean.getAddress().getPostalCode());

        AuditBuilderInfo auditBuilderInfo =
                AuditBuilderInfo.builder()
                        .params(params)
                        .description(e.getMessage())
                        .eventLogCategory("Category set from the builder")
                        .eventLogCode("Log set from the builder")
                        .result(ActionResultEnum.OK)
                        .build();

        return auditBuilderInfo;
    }

    public AuditBuilderInfo methodWithAExceptionComingFromBuildingParamsOperation(
            AuditableParamBean auditableParamBean,
            String otherParam,
            ResponseEntity<AuditableParamBean> result) {
        log.info("Calling a external buildParameters with return value");

        Map<String, String> params = new HashMap<>();
        params.put("CodigoPostalCharallave", auditableParamBean.getAddress().getPostalCode());

        return null;
    }

    public AuditBuilderInfo buildParametersForAnnotationWithOptionalImpl(
            AuditableParamBean auditableParamBean,
            String otherParam,
            ResponseEntity<AuditableParamBean> result) {
        log.info("Calling a external buildParameters with return value: otherParam:{}", otherParam);

        Map<String, String> params = new HashMap<>();
        Optional<AuditableParamBean> auditableParamBeanOptional =
                Optional.ofNullable(auditableParamBean);

        params.put(
                "CodigoPostalCharallave",
                auditableParamBeanOptional
                        .map(AuditableParamBean::getAddress)
                        .map(AuditableNestedParamBean::getPostalCode)
                        .orElse("No existe el codigo postal por NPE"));

        AuditBuilderInfo auditBuilderInfo =
                AuditBuilderInfo.builder()
                        .params(params)
                        .description(result.getBody().getName())
                        .eventLogCategory("Category set from the builder")
                        .eventLogCode("Log set from the builder")
                        .result(ActionResultEnum.OK)
                        .build();

        return auditBuilderInfo;
    }
}
