/* Decathlon (C)2023 */
package com.decathlon.rest.crud.beans;

import lombok.Data;

import java.io.Serializable;

@Data
public class PersonListDto implements Serializable {

    private static final long serialVersionUID = -5341388224130637901L;

    private Long id;
    private String name;
    private String lastName;
}
