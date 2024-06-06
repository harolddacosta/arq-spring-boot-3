/* AssentSoftware (C)2023 */
package com.decathlon.logging.audit;

import com.decathlon.logging.audit.annotation.Auditable.ActionResultEnum;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConditionalOnMissingBean(value = EventsLogger.class, ignored = SimpleEventsLogger.class)
@Slf4j
public class SimpleEventsLogger implements EventsLogger {

    @Override
    public void auditEvent(
            String eventLogCategory,
            String eventLogCode,
            ActionResultEnum result,
            String description,
            Map<String, String> params) {
        log.info(
                "-------> 'Category:{}' 'Code:{}' 'Result:{}' 'Description:{}' 'Params:{}'",
                eventLogCategory,
                eventLogCode,
                result,
                description,
                params);
    }
}
