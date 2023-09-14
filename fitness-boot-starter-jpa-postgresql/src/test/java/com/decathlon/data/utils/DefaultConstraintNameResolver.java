/* Decathlon (C)2023 */
package com.decathlon.data.utils;

import com.decathlon.data.transformer.ContraintsNameResolver;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

@Component
public class DefaultConstraintNameResolver implements ContraintsNameResolver {

    private static Map<String, String> constraintCodeMap = new HashMap<>();

    static {
        constraintCodeMap.put(
                "violates unique constraint \"uq_person_global_identification",
                "exception.document.identification.exists");
        constraintCodeMap.put(
                "violates unique constraint \"uq_constraint_found_but_no_message_configured",
                "exception.msas.exists");
    }

    @Override
    public Optional<Entry<String, String>> getConstraintName(String constraintMessage) {
        if (StringUtils.isNotBlank(constraintMessage)) {
            return constraintCodeMap.entrySet().stream()
                    .filter(it -> constraintMessage.contains(it.getKey()))
                    .findFirst();
        }

        return Optional.empty();
    }
}
