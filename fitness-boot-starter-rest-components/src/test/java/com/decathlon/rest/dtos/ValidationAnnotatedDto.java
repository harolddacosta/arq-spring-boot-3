/* AssentSoftware (C)2023 */
package com.decathlon.rest.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Data;

import java.io.Serializable;

@Data
public class ValidationAnnotatedDto implements Serializable {

    private static final long serialVersionUID = -942697419901293364L;

    @NotNull
    @Size(min = 5)
    private String name;
}
