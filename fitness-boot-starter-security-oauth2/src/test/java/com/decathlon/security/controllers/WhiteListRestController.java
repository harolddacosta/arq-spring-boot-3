/* Decathlon (C)2023 */
package com.decathlon.security.controllers;

import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WhiteListRestController {

    @GetMapping(
            value = "/swagger-ui.html",
            produces = {MediaType.TEXT_HTML_VALUE})
    public ResponseEntity<String> noJwtNeeded(HttpServletResponse response) {
        return new ResponseEntity<>(
                "<!DOCTYPE html>\n"
                        + "<html lang=\"en\">\n"
                        + "  <head>\n"
                        + "    <meta charset=\"UTF-8\">\n"
                        + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                        + "    <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">\n"
                        + "    <title>HTML 5 Boilerplate</title>\n"
                        + "    <link rel=\"stylesheet\" href=\"style.css\">\n"
                        + "  </head>\n"
                        + "  <body>\n"
                        + "	<script src=\"index.js\"></script>\n"
                        + "  </body>\n"
                        + "</html>",
                HttpStatus.OK);
    }
}
