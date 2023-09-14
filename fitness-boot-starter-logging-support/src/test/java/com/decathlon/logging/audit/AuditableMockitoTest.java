/* Decathlon (C)2023 */
package com.decathlon.logging.audit;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.decathlon.logging.audit.annotation.Auditable.ActionResultEnum;
import com.decathlon.logging.audit.annotation.AuditableAspect;
import com.decathlon.logging.audit.annotation.DefaultAuditableHandler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("deprecation")
@ExtendWith(MockitoExtension.class)
class AuditableMockitoTest {

    private static final String MSG_UNKNOWN_CODE_AUDIT = "Unknown code audit";

    private static final String MSG_UNKNOWN_CATEGORY = "Unknown category";

    @Spy private SimpleEventsLogger eventsLogger;

    @InjectMocks private DefaultAuditableHandler defaultAuditableHandler;

    private AuditableUseCases auditableUseCases;

    @BeforeEach
    void setUp() {
        AspectJProxyFactory factory = new AspectJProxyFactory(new AuditableUseCases());
        factory.addAspect(new AuditableAspect(defaultAuditableHandler));
        auditableUseCases = factory.getProxy();
    }

    @Test
    void ok_when_auditable_no_params_in_annotation() {
        auditableUseCases.auditableNoParams();

        verify(eventsLogger, times(1))
                .auditEvent(
                        "Category for non parameter event",
                        "001",
                        ActionResultEnum.OK,
                        "Simple message for audit event, no params",
                        new HashMap<>());
    }

    @Test
    void ok_when_auditable_no_params_in_annotation_but_exception() {
        assertThrows(
                RuntimeException.class, () -> auditableUseCases.auditableNoParamsWithException());

        verify(eventsLogger, times(1))
                .auditEvent(
                        "Category error for non parameter event",
                        "ERROR 001",
                        ActionResultEnum.KO,
                        "Simple error message for audit event, no params",
                        new HashMap<>());
    }

    @Test
    void ok_when_auditable_with_bean_and_properties() {
        AuditableNestedParamBean auditableNestedParamBean =
                new AuditableNestedParamBean("Cornella de Llobregat", "08940");
        AuditableParamBean auditableParamBean =
                new AuditableParamBean("Harold", new Date(81, 5, 20), 39, auditableNestedParamBean);

        auditableUseCases.auditableWithParams("param1", auditableParamBean, "param2");

        Map<String, String> parameters = new HashMap<>();
        parameters.put("birthDay", auditableParamBean.getBirthDay().toString());
        parameters.put("codigoPostal", auditableParamBean.getAddress().getPostalCode());
        parameters.put("name", auditableParamBean.getName());
        parameters.put("age", auditableParamBean.getAge().toString());

        verify(eventsLogger, times(1))
                .auditEvent(
                        "",
                        "",
                        ActionResultEnum.OK,
                        "The name of the client is {name} living in {codigoPostal}",
                        parameters);
    }

    @Test
    void ok_when_auditable_with_bean_and_external_parameter_builder() {
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        doReturn(new AuditableParameterBuilder())
                .when(applicationContext)
                .getBean("externalParameterBuilder");

        defaultAuditableHandler.setApplicationContext(applicationContext);

        AuditableNestedParamBean auditableNestedParamBean =
                new AuditableNestedParamBean("Cornella de Llobregat", "08940");
        AuditableParamBean auditableParamBean =
                new AuditableParamBean("Harold", new Date(81, 5, 20), 39, auditableNestedParamBean);

        auditableUseCases.auditableWithCustomParametersBuilding(
                "param with value 1", auditableParamBean, "param with value 2");

        Map<String, String> parameters = new HashMap<>();
        parameters.put("CodigoPostalCharallave", "08940");

        verify(eventsLogger, times(1))
                .auditEvent(
                        "Category set from the builder",
                        "Log set from the builder",
                        ActionResultEnum.OK,
                        "This description was generated from the builder",
                        parameters);
    }

    @Test
    void exception_when_auditable_with_bean_and_external_parameter_builder() {
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        doReturn(new AuditableParameterBuilder())
                .when(applicationContext)
                .getBean("externalParameterBuilder");

        defaultAuditableHandler.setApplicationContext(applicationContext);

        AuditableNestedParamBean auditableNestedParamBean =
                new AuditableNestedParamBean("Cornella de Llobregat", "08940");
        AuditableParamBean auditableParamBean =
                new AuditableParamBean("Harold", new Date(81, 5, 20), 39, auditableNestedParamBean);

        Map<String, String> parameters = new HashMap<>();
        parameters.put("CodigoPostalCharallave", "08940");

        assertThrows(
                RuntimeException.class,
                () ->
                        auditableUseCases.auditableWithCustomParametersBuildingAndException(
                                "param with value 1", auditableParamBean, "param with value 2"));

        verify(eventsLogger, times(1))
                .auditEvent(
                        "Category set from the builder when exception",
                        "Log set from the builder when exception",
                        ActionResultEnum.KO,
                        "This description was generated from the builder when exception occurs",
                        parameters);
    }

    @Test
    void ok_when_auditable_with_bean_and_external_parameter_builder_and_response_inspection() {
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        doReturn(new AuditableParameterBuilder())
                .when(applicationContext)
                .getBean("externalParameterBuilder");

        defaultAuditableHandler.setApplicationContext(applicationContext);

        AuditableNestedParamBean auditableNestedParamBean =
                new AuditableNestedParamBean("Cornella de Llobregat", "08940");
        AuditableParamBean auditableParamBean =
                new AuditableParamBean("Harold", new Date(81, 5, 20), 39, auditableNestedParamBean);

        auditableUseCases.auditableWithCustomParametersBuildingAndReturnValue(
                "param with value 1", auditableParamBean, "param with value 2");

        Map<String, String> parameters = new HashMap<>();
        parameters.put("CodigoPostalCharallave", "08940");

        verify(eventsLogger, times(1))
                .auditEvent(
                        "Category set from the builder",
                        "Log set from the builder",
                        ActionResultEnum.OK,
                        "This case will take in account this message from the response entity for the builder",
                        parameters);
    }

    @Test
    void
            ok_when_auditable_with_bean_and_external_parameter_builder_and_response_inspection_when_error() {
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        doReturn(new AuditableParameterBuilder())
                .when(applicationContext)
                .getBean("externalParameterBuilder");

        defaultAuditableHandler.setApplicationContext(applicationContext);

        AuditableNestedParamBean auditableNestedParamBean =
                new AuditableNestedParamBean("Cornella de Llobregat", "08940");
        AuditableParamBean auditableParamBean =
                new AuditableParamBean("Harold", new Date(81, 5, 20), 39, auditableNestedParamBean);

        assertThrows(
                RuntimeException.class,
                () ->
                        auditableUseCases
                                .auditableWithCustomParametersBuildingAndReturnValueAndException(
                                        "param with value 1",
                                        auditableParamBean,
                                        "param with value 2"));

        Map<String, String> parameters = new HashMap<>();
        parameters.put("CodigoPostalCharallave", "08940");

        verify(eventsLogger, times(1))
                .auditEvent(
                        "Category set from the builder",
                        "Log set from the builder",
                        ActionResultEnum.KO,
                        "Exception from internal call",
                        parameters);
    }

    @Test
    void error_when_auditable_with_bean_and_external_parameter_function_in_builder_not_found() {
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        doReturn(new AuditableParameterBuilder())
                .when(applicationContext)
                .getBean("externalParameterBuilder");

        defaultAuditableHandler.setApplicationContext(applicationContext);

        AuditableNestedParamBean auditableNestedParamBean =
                new AuditableNestedParamBean("Cornella de Llobregat", "08940");
        AuditableParamBean auditableParamBean =
                new AuditableParamBean("Harold", new Date(81, 5, 20), 39, auditableNestedParamBean);

        assertThrows(
                RuntimeException.class,
                () ->
                        auditableUseCases.auditableWithCustomParametersBuildingFunctionNotFound(
                                "param with value 1", auditableParamBean, "param with value 2"));

        verify(eventsLogger, times(1))
                .auditEvent(
                        MSG_UNKNOWN_CATEGORY,
                        MSG_UNKNOWN_CODE_AUDIT,
                        ActionResultEnum.KO,
                        "Problem inside the custom params builder for audit the event: NullPointerException: Cannot invoke \"java.lang.reflect.Method.invoke(Object, Object[])\" because \"builderMethodToExecute\" is null",
                        null);
    }

    @Test
    void error_when_auditable_with_bean_and_external_bean_not_found() {
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        doThrow(NoSuchBeanDefinitionException.class)
                .when(applicationContext)
                .getBean("beanNotFoundForAuditEvent");

        defaultAuditableHandler.setApplicationContext(applicationContext);

        AuditableNestedParamBean auditableNestedParamBean =
                new AuditableNestedParamBean("Cornella de Llobregat", "08940");
        AuditableParamBean auditableParamBean =
                new AuditableParamBean("Harold", new Date(81, 5, 20), 39, auditableNestedParamBean);

        auditableUseCases.auditableWithCustomParametersBuildingBeanNotFound(
                "param with value 1", auditableParamBean, "param with value 2");

        verify(eventsLogger, times(1))
                .auditEvent(
                        MSG_UNKNOWN_CATEGORY,
                        MSG_UNKNOWN_CODE_AUDIT,
                        ActionResultEnum.OK,
                        "You defined a custom builder bean value that can't be obtained in spring context, did you created it?",
                        null);
    }

    @Test
    void exception_when_auditable_with_bean_and_properties() {
        AuditableNestedParamBean auditableNestedParamBean =
                new AuditableNestedParamBean("Cornella de Llobregat", "08940");
        AuditableParamBean auditableParamBean =
                new AuditableParamBean("Harold", new Date(81, 5, 20), 39, auditableNestedParamBean);

        assertThrows(
                RuntimeException.class,
                () ->
                        auditableUseCases.auditableWithParamsAndException(
                                "param1", auditableParamBean, "param2"));

        Map<String, String> parameters = new HashMap<>();
        parameters.put("birthDay", auditableParamBean.getBirthDay().toString());
        parameters.put("address.postalCode", auditableParamBean.getAddress().getPostalCode());
        parameters.put("name", auditableParamBean.getName());
        parameters.put("edad", auditableParamBean.getAge().toString());

        verify(eventsLogger, times(1)).auditEvent("", "", ActionResultEnum.KO, "", parameters);
    }

    @Test
    void exception_not_rethrowed_when_auditable_with_bean_and_properties() {
        AuditableNestedParamBean auditableNestedParamBean =
                new AuditableNestedParamBean("Cornella de Llobregat", "08940");
        AuditableParamBean auditableParamBean =
                new AuditableParamBean("Harold", new Date(81, 5, 20), 39, auditableNestedParamBean);

        assertThrows(
                RuntimeException.class,
                () ->
                        auditableUseCases.auditableWithParamsAndExceptionNotCatched(
                                "param1", auditableParamBean, "param2"));

        Map<String, String> parameters = new HashMap<>();
        parameters.put("birthDay", auditableParamBean.getBirthDay().toString());
        parameters.put("address.postalCode", auditableParamBean.getAddress().getPostalCode());
        parameters.put("name", auditableParamBean.getName());
        parameters.put("age", auditableParamBean.getAge().toString());

        verify(eventsLogger, times(1))
                .auditEvent(
                        "", "", ActionResultEnum.KO, "Message when an error occurs", parameters);
    }

    @Test
    void ok_when_auditable_with_simple_property() {
        auditableUseCases.auditableWithSingleParam(
                "param1", "value of the param to audit", "param2");

        Map<String, String> parameters = new HashMap<>();
        parameters.put("paramForAudit", "value of the param to audit");

        verify(eventsLogger, times(1)).auditEvent("", "", ActionResultEnum.OK, "", parameters);
    }

    @Test
    void ok_when_auditable_with_complex_property() {
        AuditableNestedParamBean auditableNestedParamBean =
                new AuditableNestedParamBean("Cornella de Llobregat", "08940");
        AuditableParamBean auditableParamBean =
                new AuditableParamBean("Harold", new Date(81, 5, 20), 39, auditableNestedParamBean);

        auditableUseCases.auditableWithSingleComplexParam("param1", auditableParamBean, "param2");

        Map<String, String> parameters = new HashMap<>();
        parameters.put("auditableParamBean", auditableParamBean.toString());

        verify(eventsLogger, times(1)).auditEvent("", "", ActionResultEnum.OK, "", parameters);
    }

    @Test
    void
            ok_when_auditable_with_bean_and_external_parameter_builder_and_exception_from_inside_builder() {
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        doReturn(new AuditableParameterBuilder())
                .when(applicationContext)
                .getBean("externalParameterBuilder");

        defaultAuditableHandler.setApplicationContext(applicationContext);

        AuditableParamBean auditableParamBean =
                new AuditableParamBean("Harold", new Date(81, 5, 20), 39, null);

        auditableUseCases.auditableWithAExceptionComingFromBuildingParamsOperation(
                "param with value 1", auditableParamBean, "param with value 2");

        verify(eventsLogger, times(1))
                .auditEvent(
                        MSG_UNKNOWN_CATEGORY,
                        MSG_UNKNOWN_CODE_AUDIT,
                        ActionResultEnum.OK,
                        "Problem inside the custom params builder for audit the event: NullPointerException: Cannot invoke \"com.decathlon.logging.audit.AuditableNestedParamBean.getPostalCode()\" because the return value of \"com.decathlon.logging.audit.AuditableParamBean.getAddress()\" is null",
                        null);
    }

    @Test
    void ok_when_auditable_with_bean_and_external_parameter_builder_uses_optional_to_avoid_NPE() {
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        doReturn(new AuditableParameterBuilder())
                .when(applicationContext)
                .getBean("externalParameterBuilder");

        defaultAuditableHandler.setApplicationContext(applicationContext);

        AuditableParamBean auditableParamBean =
                new AuditableParamBean("Harold", new Date(81, 5, 20), 39, null);

        auditableUseCases
                .auditableWithCustomParametersBuildingAndReturnValueUsingOptionalToAvoidNPE(
                        "param with value 1", auditableParamBean, null);

        Map<String, String> parameters = new HashMap<>();
        parameters.put("CodigoPostalCharallave", "No existe el codigo postal por NPE");

        verify(eventsLogger, times(1))
                .auditEvent(
                        "Category set from the builder",
                        "Log set from the builder",
                        ActionResultEnum.OK,
                        "This case will take in account this message from the response entity for the builder",
                        parameters);
    }
}
