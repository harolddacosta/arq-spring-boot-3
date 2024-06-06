/* AssentSoftware (C)2023 */
package com.decathlon.core.exceptions;

import com.decathlon.core.response.FieldErrorResource;

import lombok.Getter;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Getter
public class LogicException extends AbstractThrowableProblem { // NOSONAR

    private static final long serialVersionUID = -5898235054808091141L;

    public static final String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";

    public static final String PARAMETER_KEY = "key";
    public static final String PARAMETER_TIME = "time";
    public static final String PARAMETER_VIOLATIONS = "violations";
    public static final String PARAMETER_KEY_PARAMETERS = "key_parameters";

    private final String translationKey;
    private final List<String> translationKeyParameters;
    private final List<FieldErrorResource> fieldErrors;

    private final URI uri;
    private final int code;
    private final String time;

    public LogicException(String title, String detail, String translationKey, URI uri, int code) {
        super(
                null,
                title,
                Status.BAD_REQUEST,
                detail,
                null,
                null,
                Map.of(
                        PARAMETER_KEY,
                        translationKey,
                        PARAMETER_TIME,
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT))));

        this.translationKey = translationKey;
        this.translationKeyParameters = null;
        this.fieldErrors = List.of();
        this.time = (String) getParameters().get(PARAMETER_TIME);
        this.uri = uri;
        this.code = code;
    }

    public LogicException(
            String title,
            String detail,
            String translationKey,
            List<String> translationKeyParameters,
            URI uri,
            int code) {
        super(
                null,
                title,
                Status.BAD_REQUEST,
                detail,
                null,
                null,
                Map.of(
                        PARAMETER_KEY,
                        translationKey,
                        PARAMETER_TIME,
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)),
                        PARAMETER_KEY_PARAMETERS,
                        translationKeyParameters));

        this.translationKey = translationKey;
        this.translationKeyParameters = translationKeyParameters;
        this.fieldErrors = List.of();
        this.time = (String) getParameters().get(PARAMETER_TIME);
        this.uri = uri;
        this.code = code;
    }

    public LogicException(
            String title,
            String detail,
            List<FieldErrorResource> fieldErrors,
            String translationKey,
            URI uri,
            int code) {
        super(
                null,
                title,
                Status.BAD_REQUEST,
                detail,
                null,
                null,
                Map.of(
                        PARAMETER_KEY,
                        translationKey,
                        PARAMETER_TIME,
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)),
                        PARAMETER_VIOLATIONS,
                        Collections.unmodifiableList(fieldErrors)));

        this.translationKey = translationKey;
        this.translationKeyParameters = null;
        this.fieldErrors = Collections.unmodifiableList(fieldErrors);
        this.time = (String) getParameters().get(PARAMETER_TIME);
        this.uri = uri;
        this.code = code;
    }

    public LogicException(
            String title,
            String detail,
            List<FieldErrorResource> fieldErrors,
            String translationKey,
            List<String> translationKeyParameters,
            URI uri,
            int code) {
        super(
                null,
                title,
                Status.BAD_REQUEST,
                detail,
                null,
                null,
                Map.of(
                        PARAMETER_KEY,
                        translationKey,
                        PARAMETER_TIME,
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)),
                        PARAMETER_VIOLATIONS,
                        Collections.unmodifiableList(fieldErrors),
                        PARAMETER_KEY_PARAMETERS,
                        translationKeyParameters));

        this.translationKey = translationKey;
        this.translationKeyParameters = translationKeyParameters;
        this.fieldErrors = Collections.unmodifiableList(fieldErrors);
        this.time = (String) getParameters().get(PARAMETER_TIME);
        this.uri = uri;
        this.code = code;
    }
}
