/* Decathlon (C)2023 */
package com.decathlon.openapi.annotations;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Parameter(
        name = "page",
        in = ParameterIn.QUERY,
        description = "The page to be returned",
        schema = @Schema(type = "integer"))
@Parameter(
        name = "size",
        in = ParameterIn.QUERY,
        description = "The number of items of that page",
        schema = @Schema(type = "integer"))
@Parameter(
        name = "sort",
        in = ParameterIn.QUERY,
        description = "The fields to sort eg. id,desc <-- no space after comma!",
        schema = @Schema(type = "string"))
@Parameter(name = "pageable", hidden = true)
@Parameter(name = "pagination", hidden = true)
public @interface PageableSwagger {}
