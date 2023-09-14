/* Decathlon (C)2023 */
package com.decathlon.rest.crud.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.Optional;

public interface CrudService<E extends Serializable> {

    Optional<E> findById(Long id);

    E save(E entityToSave);

    E update(E entityToUpdate);

    void delete(E entityToUpdate);

    Page<E> findLike(String globalFilter, Pageable pagination);
}
