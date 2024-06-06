/* AssentSoftware (C)2023 */
package com.decathlon.rest.web.client;

import com.decathlon.core.exceptions.LogicException;
import com.decathlon.core.response.FieldErrorResource;
import com.decathlon.rest.utils.ExceptionUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.zalando.problem.Problem;
import org.zalando.problem.ProblemBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CustomResponseErrorHandler extends DefaultResponseErrorHandler {

    private static final String KEY_VIOLATIONS = "violations";
    private static final String KEY_FOR_KEY_PARAMETERS = "key_parameters";
    private static final String KEY_FRONT_END_CODE = "front_end_code";

    private final ObjectMapper objectMapper;

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        MediaType mediaTypeFromResponse = response.getHeaders().getContentType();

        if (mediaTypeFromResponse != null
                && mediaTypeFromResponse.getSubtype().equalsIgnoreCase("problem+json")) {
            byte[] body = getResponseBody(response);

            Optional<Problem> errorResponseInProblem =
                    ExceptionUtils.convertFromHttpClientErrorException(
                            body, objectMapper, Problem.class);

            if (errorResponseInProblem.isPresent()) {
                handleCustomException(errorResponseInProblem.get());
            }
        }

        super.handleError(response);
    }

    private void handleCustomException(Problem exception) {
        ProblemBuilder problemBuilder = Problem.builder();

        if (exception.getParameters() != null && !exception.getParameters().isEmpty()) {
            if (exception.getParameters().get(KEY_FRONT_END_CODE) != null
                    && 2000 == (int) exception.getParameters().get(KEY_FRONT_END_CODE)) {
                buildAndThrowLogicException(exception);
            }

            exception.getParameters().forEach(problemBuilder::with);
        }

        throw problemBuilder
                .withTitle(exception.getTitle())
                .withStatus(exception.getStatus())
                .withDetail(exception.getDetail())
                .withInstance(exception.getInstance())
                .withType(exception.getType())
                .build();
    }

    private void buildAndThrowLogicException(Problem problem) {
        URI uri = null;
        int code = 0;

        if (problem.getParameters().get("url") != null) {
            uri = URI.create((String) problem.getParameters().get("url"));
        }

        if (problem.getParameters().get(KEY_FRONT_END_CODE) != null) {
            code = (int) problem.getParameters().get(KEY_FRONT_END_CODE);
        }

        throw getExactLogicExceptionType(problem, uri, code);
    }

    @SuppressWarnings("unchecked")
    private LogicException getExactLogicExceptionType(Problem problem, URI uri, int code) {
        LogicException exception =
                new LogicException(
                        problem.getTitle(),
                        problem.getDetail(),
                        (String) problem.getParameters().get("key"),
                        uri,
                        code);

        if (problem.getParameters().get(KEY_FOR_KEY_PARAMETERS) != null
                && problem.getParameters().get(KEY_VIOLATIONS) != null) {
            exception =
                    new LogicException(
                            problem.getTitle(),
                            problem.getDetail(),
                            transform(
                                    (ArrayList<LinkedHashMap<String, String>>)
                                            problem.getParameters().get(KEY_VIOLATIONS),
                                    FieldErrorResource.class),
                            (String) problem.getParameters().get("key"),
                            (List<String>) problem.getParameters().get(KEY_FOR_KEY_PARAMETERS),
                            uri,
                            code);
        } else if (problem.getParameters().get(KEY_FOR_KEY_PARAMETERS) == null
                && problem.getParameters().get(KEY_VIOLATIONS) != null) {
            exception =
                    new LogicException(
                            problem.getTitle(),
                            problem.getDetail(),
                            transform(
                                    (ArrayList<LinkedHashMap<String, String>>)
                                            problem.getParameters().get(KEY_VIOLATIONS),
                                    FieldErrorResource.class),
                            (String) problem.getParameters().get("key"),
                            uri,
                            code);
        } else if (problem.getParameters().get(KEY_FOR_KEY_PARAMETERS) != null
                && problem.getParameters().get(KEY_VIOLATIONS) == null) {
            exception =
                    new LogicException(
                            problem.getTitle(),
                            problem.getDetail(),
                            (String) problem.getParameters().get("key"),
                            (List<String>) problem.getParameters().get(KEY_FOR_KEY_PARAMETERS),
                            uri,
                            code);
        }

        return exception;
    }

    private <T> List<T> transform(
            ArrayList<LinkedHashMap<String, String>> fieldErrorslist, Class<T> type) {
        return fieldErrorslist.stream().map(m -> objectMapper.convertValue(m, type)).toList();
    }
}
