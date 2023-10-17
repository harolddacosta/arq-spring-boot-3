/* Decathlon (C)2023 */
package com.decathlon.rest.crud.repositories;

import com.decathlon.rest.crud.entities.Persona;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PersonRepository {

    public Optional<Persona> findById(Long id) {
        return null;
    }

    public Persona save(Persona entityToSave) {
        if (entityToSave.getId() == null) {
            entityToSave.setId(Long.valueOf(RandomStringUtils.randomNumeric(5)));
        }

        entityToSave.setVersion(entityToSave.getVersion() + 1);

        return entityToSave;
    }

    public Page<Persona> findPersonsLike(String globalFilter, Pageable pageable) {
        return null;
    }
}
