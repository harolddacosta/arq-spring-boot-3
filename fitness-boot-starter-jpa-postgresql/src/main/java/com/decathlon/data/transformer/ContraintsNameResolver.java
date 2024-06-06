/* AssentSoftware (C)2023 */
package com.decathlon.data.transformer;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

public interface ContraintsNameResolver {

    Map<String, String> getConstraintCodeMap();

    default Optional<Entry<String, String>> getConstraintName(String constraintMessage) {
        if (StringUtils.isNotBlank(constraintMessage)) {
            return getConstraintCodeMap().entrySet().stream()
                    .filter(it -> constraintMessage.contains(it.getKey()))
                    .findFirst();
        }

        return Optional.empty();
    }
}
