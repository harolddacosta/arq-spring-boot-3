/* Decathlon (C)2023 */
package com.decathlon.rest.controllers;

import com.decathlon.core.exceptions.LogicException;
import com.decathlon.rest.utils.RequestUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.Problem;
import org.zalando.problem.ProblemBuilder;
import org.zalando.problem.spring.web.advice.AdviceTrait;

@RestControllerAdvice
@SuppressWarnings({"squid:S1166"})
@RequiredArgsConstructor
@Slf4j
@Order(-100)
public class DefaultRestExceptionHandlerController implements AdviceTrait {

    private static final String PARAMETER_FRONT_END_CODE = "front_end_code";

    public static final String BUILDING_ERROR_RESPONSE = "Building ErrorResponse:";

    private final MessageSource messageSource;

    @ExceptionHandler(LogicException.class)
    public ResponseEntity<Problem> exceptionHandler(
            LogicException exception, NativeWebRequest request) {
        log.error(BUILDING_ERROR_RESPONSE, exception);

        String translatedDetail = null;

        try {
            translatedDetail =
                    messageSource.getMessage(
                            exception.getTranslationKey(),
                            exception.getTranslationKeyParameters() != null
                                            && !exception.getTranslationKeyParameters().isEmpty()
                                    ? exception.getTranslationKeyParameters().toArray(String[]::new)
                                    : null,
                            LocaleContextHolder.getLocale());
        } catch (NoSuchMessageException e) {
            // Do nothing, keep the original message
        }

        if (exception.getFieldErrors() != null && !exception.getFieldErrors().isEmpty()) {
            translateFieldErrors(exception);
        }

        ProblemBuilder problemBuilder =
                Problem.builder()
                        .withTitle(exception.getTitle())
                        .withStatus(exception.getStatus())
                        .withDetail(
                                translatedDetail != null ? translatedDetail : exception.getDetail())
                        .with(PARAMETER_FRONT_END_CODE, 2000);

        RequestUtils.addRequestAdditionalInformation(problemBuilder);

        if (exception.getParameters() != null && !exception.getParameters().isEmpty()) {
            addParametersToException(exception, problemBuilder);
        }

        return create(problemBuilder.build(), request);
    }

    private void addParametersToException(LogicException exception, ProblemBuilder problemBuilder) {
        exception.getParameters().forEach(problemBuilder::with);
    }

    private void translateFieldErrors(LogicException exception) {
        exception
                .getFieldErrors()
                .forEach(
                        fieldErrorResource -> {
                            try {
                                fieldErrorResource.setDetail(
                                        messageSource.getMessage(
                                                fieldErrorResource.getTranslationKey(),
                                                fieldErrorResource.getTranslationKeyParameters(),
                                                LocaleContextHolder.getLocale()));
                            } catch (NoSuchMessageException e) {
                                // Do nothing, keep the original message
                            }
                        });
    }
}
