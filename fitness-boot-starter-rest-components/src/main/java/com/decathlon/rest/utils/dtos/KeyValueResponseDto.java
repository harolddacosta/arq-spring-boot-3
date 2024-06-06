/* AssentSoftware (C)2023 */
package com.decathlon.rest.utils.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeyValueResponseDto implements Serializable {

    private static final long serialVersionUID = -942697419901293364L;

    private String value;
}
