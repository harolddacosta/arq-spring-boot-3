/* AssentSoftware (C)2023 */
package com.decathlon.rest.controllers;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

@ControllerAdvice
@ConditionalOnProperty(
        prefix = "spring.mvc",
        name = "throw-exception-if-no-handler-found",
        havingValue = "true")
public class SinglePageApplicationController {

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<String> renderDefaultPage() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/public/index.html");
            String body = StreamUtils.copyToString(inputStream, Charset.defaultCharset());

            return ResponseEntity.ok().body(body);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("There was an error completing the action.");
        }
    }
}
