/* Decathlon (C)2023 */
package com.decathlon.rest.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class DatesDto implements Serializable {

    private static final long serialVersionUID = -6486321779148289697L;

    private Long id;
    private String name;
    private Date birthDate;

    /** This overwrites the global one */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private Date jsonFormatDate;

    /** This overwrites the global one */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate jsonFormatLocalDate;

    //	@JsonSerialize(using = LocalDateSerializer.class)
    //	@JsonDeserialize(using = LocalDateDeserializer.class)
    //	@JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate localDateField;

    //	@JsonSerialize(using = LocalDateTimeSerializer.class)
    //	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
    //	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime localDateTimeField;

    private Date oldDateField;
}
