/* AssentSoftware (C)2023 */
package com.decathlon.logging.dto;

import com.decathlon.core.domain.types.Identifiable;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PersonDto implements Identifiable, Serializable {

    private static final long serialVersionUID = -8293952137314357678L;

    private Long id;

    @Size(max = 16)
    private String code;

    @NotNull
    @Size(max = 64)
    private String name;

    @NotNull
    @Size(max = 64)
    private String lastName;

    @NotNull private Long identityDocumentTypeId;

    @NotNull
    @Size(max = 64)
    private String identityDocument;

    @Size(max = 64)
    private String sanitaryDocument;

    @NotNull
    @Size(max = 32)
    private String mobilePhone;

    @Size(max = 32)
    private String homePhone;

    @Email
    @Size(max = 64)
    private String email;

    @NotNull private String addressLine1;
    private String addressLine2;

    private Long provinceId;
    private Long municipalityId;
    @NotNull private Long postalCodeId;

    @Past private LocalDate birthDate;

    @Min(0)
    private Float age;

    private String url1FileName;

    private Long sexId;
    private Long bloodGroupId;
    private Long countryBirthId;

    private Long version;
}
