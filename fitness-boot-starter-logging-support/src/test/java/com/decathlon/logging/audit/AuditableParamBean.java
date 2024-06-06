/* AssentSoftware (C)2023 */
package com.decathlon.logging.audit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AuditableParamBean {

    private String name;
    private Date birthDay;
    private Integer age;
    private AuditableNestedParamBean address;
}
