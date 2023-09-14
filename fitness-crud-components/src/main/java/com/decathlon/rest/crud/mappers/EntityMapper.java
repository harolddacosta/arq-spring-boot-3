/* Decathlon (C)2023 */
package com.decathlon.rest.crud.mappers;

import java.io.Serializable;
import java.util.List;

public interface EntityMapper<
        D extends Serializable, L extends Serializable, E extends Serializable> {

    D fromEntityToDto(E entity);

    E fromDtoToEntityForSave(D dto);

    E mergeDtoIntoEntity(D dto, E entity);

    E patchDtoIntoEntity(D dto, E entity);

    List<L> entitiesToDtos(List<E> content);
}
