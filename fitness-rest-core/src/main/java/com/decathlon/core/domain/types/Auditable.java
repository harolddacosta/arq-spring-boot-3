/* Decathlon (C)2023 */
package com.decathlon.core.domain.types;

import java.time.LocalDateTime;

public interface Auditable {

    String getCreatedBy();

    void setCreatedBy(String usuarioCreacion);

    LocalDateTime getCreatedAt();

    void setCreatedAt(LocalDateTime fechaCreacion);

    String getLastModifiedBy();

    void setLastModifiedBy(String usuarioModificacion);

    LocalDateTime getLastModifiedDate();

    void setLastModifiedDate(LocalDateTime fechaModificacion);
}
