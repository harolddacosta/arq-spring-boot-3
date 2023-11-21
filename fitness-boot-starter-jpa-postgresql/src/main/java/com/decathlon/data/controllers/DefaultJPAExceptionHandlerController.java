/* Decathlon (C)2023 */
package com.decathlon.data.controllers;

import com.decathlon.core.exceptions.ResourceNotFoundException;
import com.decathlon.data.transformer.ContraintsNameResolver;

import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.ObjectNotFoundException;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.Problem;
import org.zalando.problem.ProblemBuilder;
import org.zalando.problem.Status;
import org.zalando.problem.spring.web.advice.AdviceTrait;

import java.util.Map;
import java.util.Optional;

@RestControllerAdvice
@RequiredArgsConstructor
@Order(-200)
public class DefaultJPAExceptionHandlerController implements AdviceTrait {

    private static final String PROBLEM_CONSTRAINT_KEY = "constraint_key";

    protected final MessageSource messageSource;
    protected final ContraintsNameResolver contraintsNameResolver;

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Problem> dataIntegrityViolationExceptionHandler(
            DataIntegrityViolationException exception, NativeWebRequest request) {
        String rootMsg = ExceptionUtils.getRootCause(exception).getMessage();
        ProblemBuilder problemBuilder =
                Problem.builder().withTitle("Conflict").withStatus(Status.CONFLICT);

        if (StringUtils.isNotBlank(rootMsg)) {
            Optional<Map.Entry<String, String>> entry =
                    contraintsNameResolver.getConstraintName(rootMsg);

            if (entry.isPresent()) {
                String exceptionTranslation = entry.get().getValue();

                try {
                    exceptionTranslation =
                            messageSource.getMessage(
                                    entry.get().getValue(), null, LocaleContextHolder.getLocale());
                } catch (NoSuchMessageException e) {
                    // Do nothing, keep the original message
                }

                problemBuilder
                        .withDetail("Data constraint violation: '" + exceptionTranslation + "'")
                        .with(PROBLEM_CONSTRAINT_KEY, entry.get().getValue());
            } else {
                problemBuilder
                        .withDetail(
                                "Data constraint violation: foreign key found '"
                                        + rootMsg
                                        + "' but not found the description for the constraint in the resource bundle")
                        .with(PROBLEM_CONSTRAINT_KEY, "exception.constraint.translation.undefined");
            }
        } else {
            problemBuilder
                    .withDetail(
                            "Data constraint violation: not found the description for the constraint")
                    .with(PROBLEM_CONSTRAINT_KEY, "exception.constraint.undefined");
        }

        return create(problemBuilder.build(), request);
    }

    @ExceptionHandler(
            value = {
                ObjectNotFoundException.class,
                EntityNotFoundException.class,
                ResourceNotFoundException.class
            })
    public ResponseEntity<Problem> handleNotFoundOperation(
            final Exception exception, final NativeWebRequest request) {
        return create(Status.NOT_FOUND, exception, request);
    }
}
