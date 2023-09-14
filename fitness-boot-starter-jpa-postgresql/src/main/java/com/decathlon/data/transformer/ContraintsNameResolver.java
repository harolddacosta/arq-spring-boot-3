/* Decathlon (C)2023 */
package com.decathlon.data.transformer;

import java.util.Map;
import java.util.Optional;

public interface ContraintsNameResolver {

    Optional<Map.Entry<String, String>> getConstraintName(String constraintMessage);
}
