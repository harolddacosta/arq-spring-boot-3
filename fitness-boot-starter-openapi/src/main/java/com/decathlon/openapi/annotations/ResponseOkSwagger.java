/* Decathlon (C)2023 */
package com.decathlon.openapi.annotations;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "You are not authenticated"),
            @ApiResponse(
                    responseCode = "403",
                    description = "You are not authorized to see these this"),
            @ApiResponse(responseCode = "404", description = "Not found")
        })
public @interface ResponseOkSwagger {}
