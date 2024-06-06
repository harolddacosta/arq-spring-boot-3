/* AssentSoftware (C)2023 */
package com.decathlon.logging.audit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AuditableNestedParamBean {

    private String address;
    private String postalCode;
}
