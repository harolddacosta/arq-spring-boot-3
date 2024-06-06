/* AssentSoftware (C)2023 */
package com.decathlon.rest.controllers;

import com.decathlon.core.exceptions.LogicException;
import com.decathlon.core.response.FieldErrorResource;
import com.decathlon.rest.dtos.ValidationAnnotatedDto;
import com.decathlon.rest.utils.dtos.KeyValueResponseDto;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api/v1/exception-handling")
public class ExceptionHandlingRestController {

    @GetMapping("/logic-exception")
    public ResponseEntity<String> logicException() {
        throw new LogicException(
                "Exception title",
                "Exception detail",
                "error.logic-exception",
                URI.create("www.google.com"),
                2000);
    }

    @GetMapping("/logic-exception-key-parameters")
    public ResponseEntity<String> logicExceptionWithKeyParameters() {
        throw new LogicException(
                "Exception title",
                "Exception detail",
                "error.logic-exception",
                List.of("param1", "param2"),
                URI.create("www.google.com"),
                2000);
    }

    @GetMapping("/logic-exception-key-parameters-and-violations")
    public ResponseEntity<String> logicExceptionWithKeyParametersAndViolations() {
        FieldErrorResource fer =
                new FieldErrorResource(
                        "FieldError title", "FieldError detail", "error.field.logic-exception");

        throw new LogicException(
                "Exception title",
                "Exception detail",
                Arrays.asList(fer),
                "error.logic-exception",
                List.of("param1", "param2"),
                URI.create("www.google.com"),
                2000);
    }

    @GetMapping("/logic-exception-fields")
    public ResponseEntity<String> logicExceptionWithFields() {
        FieldErrorResource fer =
                new FieldErrorResource(
                        "FieldError title", "FieldError detail", "error.field.logic-exception");

        throw new LogicException(
                "Exception title",
                "Exception detail",
                Arrays.asList(fer),
                "error.logic-exception",
                URI.create("www.google.com"),
                2000);
    }

    @GetMapping("/logic-exception-translated")
    public ResponseEntity<String> logicExceptionTranslated() {
        throw new LogicException(
                "Exception title",
                "Exception detail",
                "error.logic-exception-translated",
                URI.create("www.google.com"),
                2000);
    }

    @GetMapping("/logic-exception-translated-fields")
    public ResponseEntity<String> logicExceptionTranslatedWithField() {
        FieldErrorResource fer =
                new FieldErrorResource(
                        "FieldError title",
                        "FieldError detail",
                        "error.field.logic-exception-translated");

        throw new LogicException(
                "Exception title",
                "Exception detail",
                Arrays.asList(fer),
                "error.logic-exception-translated",
                URI.create("www.google.com"),
                2000);
    }

    @GetMapping("/missing-param")
    public ResponseEntity<KeyValueResponseDto> missingParam(
            @RequestParam(name = "key") String missingParam,
            @RequestParam(name = "other_key") String otherKeyParam) {
        return new ResponseEntity<>(new KeyValueResponseDto("Harold"), HttpStatus.OK);
    }

    @PostMapping("/validate-dto")
    public ResponseEntity<KeyValueResponseDto> validateDto(
            @Valid @RequestBody ValidationAnnotatedDto annotatedDto) {
        return new ResponseEntity<>(new KeyValueResponseDto("Harold"), HttpStatus.OK);
    }

    @PostMapping("/validate-dto-no-annotation")
    public ResponseEntity<KeyValueResponseDto> validateDtoNoAnnotation(
            @RequestBody ValidationAnnotatedDto annotatedDto) {

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        // validator.validate(annotatedDto);

        throw new ConstraintViolationException(validator.validate(annotatedDto));
    }

    @PostMapping("/exceed-file-size")
    public ResponseEntity<KeyValueResponseDto> validateFileSizeExceeded(
            @RequestPart("file") MultipartFile file) {
        throw new MultipartException("This excedede size");
    }

    @GetMapping("/single-status-error-response")
    public ResponseEntity<Problem> singleStatusErrorResponse() {
        throw Problem.valueOf(Status.SERVICE_UNAVAILABLE, "Database not reachable");
    }

    @GetMapping("/single-status-error-bigger-response")
    public ResponseEntity<byte[]> singleStatusErrorBiggerResponse() {
        byte[] b = new byte[500];
        new Random().nextBytes(b);

        return new ResponseEntity<>(b, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @GetMapping("/npe")
    public ResponseEntity<KeyValueResponseDto> npeResponse() {
        throw new NullPointerException("NPE registered");
    }
}
