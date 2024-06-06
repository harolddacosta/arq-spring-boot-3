/* AssentSoftware (C)2023 */
package com.decathlon.logging.audit.annotation;

import com.decathlon.logging.audit.annotation.Auditable.ActionResultEnum;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Builder
@Getter
public class AuditBuilderInfo {

    private String eventLogCategory;
    private String eventLogCode;
    private ActionResultEnum result;
    private Map<String, String> params;
    private String description;
}
