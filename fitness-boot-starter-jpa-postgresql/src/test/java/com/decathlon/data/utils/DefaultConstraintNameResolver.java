/* Decathlon (C)2023 */
package com.decathlon.data.utils;

import com.decathlon.data.transformer.ContraintsNameResolver;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DefaultConstraintNameResolver implements ContraintsNameResolver {

    private static final Map<String, String> constraintCodeMap = new HashMap<>();

    static {
        constraintCodeMap.put(
                "violates unique constraint \"uq_person_global_identification",
                "exception.document.identification.exists");
        constraintCodeMap.put(
                "violates unique constraint \"uq_constraint_found_but_no_message_configured",
                "exception.msas.exists");
    }

    @Override
    public Map<String, String> getConstraintCodeMap() {
        return constraintCodeMap;
    }
}
