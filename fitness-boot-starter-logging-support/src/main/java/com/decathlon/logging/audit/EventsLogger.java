/* Decathlon (C)2023 */
package com.decathlon.logging.audit;

import com.decathlon.logging.audit.annotation.Auditable.ActionResultEnum;

import java.util.Map;

public interface EventsLogger {

    void auditEvent(
            String eventLogCategory,
            String eventLogCode,
            ActionResultEnum result,
            String description,
            Map<String, String> params);
}
