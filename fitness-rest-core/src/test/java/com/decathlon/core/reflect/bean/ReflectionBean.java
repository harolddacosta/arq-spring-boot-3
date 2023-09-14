/* Decathlon (C)2023 */
package com.decathlon.core.reflect.bean;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
public class ReflectionBean implements Serializable {

    private static final long serialVersionUID = 6631033103320985986L;

    private String stringValue;
    private Date dateValue;
    private LocalDate localDateValue;
    private Float floatValue;
    private Long longValue;
}
