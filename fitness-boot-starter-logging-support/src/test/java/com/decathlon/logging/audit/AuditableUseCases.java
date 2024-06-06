/* AssentSoftware (C)2023 */
package com.decathlon.logging.audit;

import com.decathlon.logging.audit.annotation.Auditable;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;

import java.util.Date;

@SuppressWarnings("deprecation")
@Slf4j
public class AuditableUseCases {

    @Auditable(
            category = "Category for non parameter event",
            code = "001",
            message = "Simple message for audit event, no params")
    public void auditableNoParams() {
        log.info("**** First line of function with no params");

        internalCall();

        log.info("**** Last line of function");
    }

    @Auditable(
            categoryWhenErrorOccurs = "Category error for non parameter event",
            codeWhenErrorOccurs = "ERROR 001",
            messageWhenErrorOccurs = "Simple error message for audit event, no params")
    public void auditableNoParamsWithException() {
        log.info("**** First line of function with no params");

        internalCallWitException();

        log.info("**** Last line of function");
    }

    @Auditable(
            target = "auditableParamBean",
            fields = {"name", "birthDay", "age", "codigoPostal::address.postalCode"},
            message = "The name of the client is {name} living in {codigoPostal}")
    public void auditableWithParams(
            String otherParam, AuditableParamBean auditableParamBean, String otherPositionParam) {
        log.info("**** First line of function with params and fields");

        internalCall();

        log.info("**** Last line of function");
    }

    @Auditable(
            parameterBuilderBeanName = "externalParameterBuilder",
            parametersBuilderFunctionName = "buildParametersForAnnotation",
            parametersBuilderNames = {"auditableParamBean", "otherPositionParam"})
    public void auditableWithCustomParametersBuilding(
            String otherParam, AuditableParamBean auditableParamBean, String otherPositionParam) {
        log.info("**** First line of function with custom parameter builder with no result object");

        internalCall();

        log.info("**** Last line of function");
    }

    @Auditable(
            parameterBuilderBeanName = "externalParameterBuilder",
            parametersBuilderFunctionName = "buildParametersForAnnotation",
            parametersBuilderNames = {"auditableParamBean", "otherPositionParam"})
    public void auditableWithCustomParametersBuildingAndException(
            String otherParam, AuditableParamBean auditableParamBean, String otherPositionParam) {
        log.info(
                "**** First line of function with custom parameter builder with no result object and exception");

        internalCallWitException();

        log.info("**** Last line of function");
    }

    @Auditable(
            parameterBuilderBeanName = "externalParameterBuilder",
            parametersBuilderFunctionName = "buildParametersForAnnotationWithReturnValue",
            parametersBuilderNames = {"auditableParamBean", "otherPositionParam"})
    public ResponseEntity<AuditableParamBean> auditableWithCustomParametersBuildingAndReturnValue(
            String otherParam, AuditableParamBean auditableParamBean, String otherPositionParam) {
        log.info("**** First line of function with custom parameter builder");

        internalCall();

        log.info("**** Last line of function");

        return ResponseEntity.ok(
                new AuditableParamBean(
                        "This case will take in account this message from the response entity for the builder",
                        new Date(81, 5, 20),
                        50,
                        null));
    }

    @Auditable(
            parameterBuilderBeanName = "externalParameterBuilder",
            parametersBuilderFunctionName = "buildParametersForAnnotationWithReturnValue",
            parametersBuilderNames = {"auditableParamBean", "otherPositionParam"})
    public ResponseEntity<AuditableParamBean>
            auditableWithCustomParametersBuildingAndReturnValueAndException(
                    String otherParam,
                    AuditableParamBean auditableParamBean,
                    String otherPositionParam) {
        log.info(
                "**** First line of function with custom parameter builder but internal exception and return a response entity");

        internalCallWitException();

        log.info("**** Last line of function");

        return ResponseEntity.ok(
                new AuditableParamBean(
                        "This case will take in account this message for the builder",
                        new Date(81, 5, 20),
                        50,
                        null));
    }

    @Auditable(
            parameterBuilderBeanName = "externalParameterBuilder",
            parametersBuilderFunctionName = "notMethodForExceptionThreatment",
            parametersBuilderNames = {"auditableParamBean", "otherPositionParam"})
    public ResponseEntity<AuditableParamBean> auditableWithCustomParametersBuildingFunctionNotFound(
            String otherParam, AuditableParamBean auditableParamBean, String otherPositionParam) {
        log.info(
                "**** First line of function with custom parameter builder but internal exception and return a response entity");

        internalCallWitException();

        log.info("**** Last line of function");

        return ResponseEntity.ok(
                new AuditableParamBean(
                        "This case will take in account this message for the builder",
                        new Date(81, 5, 20),
                        50,
                        null));
    }

    @Auditable(
            parameterBuilderBeanName = "beanNotFoundForAuditEvent",
            parametersBuilderFunctionName = "notMethodForExceptionThreatment",
            parametersBuilderNames = {"auditableParamBean", "otherPositionParam"})
    public ResponseEntity<AuditableParamBean> auditableWithCustomParametersBuildingBeanNotFound(
            String otherParam, AuditableParamBean auditableParamBean, String otherPositionParam) {
        log.info(
                "**** First line of function with custom parameter builder but internal exception and return a response entity");

        internalCall();

        log.info("**** Last line of function");

        return ResponseEntity.ok(
                new AuditableParamBean(
                        "This case will take in account this message for the builder",
                        new Date(81, 5, 20),
                        50,
                        null));
    }

    @Auditable(
            target = "auditableParamBean",
            fields = {"name", "birthDay", "edad::age", "address.postalCode"})
    public void auditableWithParamsAndException(
            String otherParam, AuditableParamBean auditableParamBean, String otherPositionParam) {
        log.info("**** First line of function that catches an exception and re-throw it");

        try {
            internalCallWitException();
        } catch (Exception e) {
            log.error("Exception catched in controller before aspect executes");

            throw e;
        }

        log.info("**** Last line of function");
    }

    @Auditable(
            target = "auditableParamBean",
            fields = {"name", "birthDay", "age", "address.postalCode"},
            messageWhenErrorOccurs = "Message when an error occurs")
    public void auditableWithParamsAndExceptionNotCatched(
            String otherParam, AuditableParamBean auditableParamBean, String otherPositionParam) {
        log.info("**** First line of function logs an internal throwed exception");

        internalCallWitException();

        log.info("**** Last line of function");
    }

    @Auditable(target = "paramForAudit")
    public void auditableWithSingleParam(
            String otherParam, String paramForAudit, String otherPositionParam) {
        log.info("**** First line of function with single string param to print");

        internalCall();

        log.info("**** Last line of function");
    }

    @Auditable(target = "auditableParamBean")
    public void auditableWithSingleComplexParam(
            String string, AuditableParamBean auditableParamBean, String string2) {
        log.info("**** First line of function with single complex param");

        internalCall();

        log.info("**** Last line of function");
    }

    @Auditable(
            parameterBuilderBeanName = "externalParameterBuilder",
            parametersBuilderFunctionName = "methodWithAExceptionComingFromBuildingParamsOperation",
            parametersBuilderNames = {"auditableParamBean", "otherPositionParam"})
    public ResponseEntity<AuditableParamBean>
            auditableWithAExceptionComingFromBuildingParamsOperation(
                    String otherParam,
                    AuditableParamBean auditableParamBean,
                    String otherPositionParam) {
        log.info(
                "**** First line of function with custom parameter builder but internal exception and return a response entity");

        internalCall();

        log.info("**** Last line of function");

        return ResponseEntity.ok(
                new AuditableParamBean(
                        "This case will take in account this message for the builder",
                        new Date(81, 5, 20),
                        50,
                        null));
    }

    @Auditable(
            parameterBuilderBeanName = "externalParameterBuilder",
            parametersBuilderFunctionName = "buildParametersForAnnotationWithOptionalImpl",
            parametersBuilderNames = {"auditableParamBean", "otherPositionParam"})
    public ResponseEntity<AuditableParamBean>
            auditableWithCustomParametersBuildingAndReturnValueUsingOptionalToAvoidNPE(
                    String otherParam,
                    AuditableParamBean auditableParamBean,
                    String otherPositionParam) {
        log.info(
                "**** First line of function with custom parameter builder using Optional inside builder");

        internalCall();

        log.info("**** Last line of function");

        return ResponseEntity.ok(
                new AuditableParamBean(
                        "This case will take in account this message from the response entity for the builder",
                        new Date(81, 5, 20),
                        50,
                        null));
    }

    private void internalCall() {
        log.info("**** Internal call");
    }

    private void internalCallWitException() {
        log.info("**** Internal call with exception");

        throw new RuntimeException("Exception from internal call");
    }
}
