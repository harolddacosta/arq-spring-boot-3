/* Decathlon (C)2023 */
package com.decathlon.rest.crud.service;

import com.decathlon.rest.crud.entities.Persona;
import com.decathlon.rest.crud.repositories.PersonRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    @Override
    public Optional<Persona> findById(Long id) {
        return personRepository.findById(id);
    }

    @Override
    public Persona save(Persona entityToSave) {
        log.debug("Value of entity for saving:{}", entityToSave);

        preValidationsOnPersonUpdate(entityToSave);

        entityToSave.setCodigoPersona(buildCode());

        return personRepository.save(entityToSave);
    }

    @Override
    public Persona update(Persona entityToMerge) {
        preValidationsOnPersonUpdate(entityToMerge);

        return personRepository.save(entityToMerge);
    }

    @Override
    public void delete(Persona entityToUpdate) {
        // Do nothing
    }

    @Override
    public Page<Persona> findLike(String globalFilter, Pageable pageable) {
        return personRepository.findPersonsLike(globalFilter, pageable);
    }

    private String buildCode() {
        return RandomStringUtils.randomNumeric(5);
    }

    private void preValidationsOnPersonUpdate(Persona entityToSave) {
        if (StringUtils.isBlank(entityToSave.getTelefonoFijoPersona())) {
            entityToSave.setTelefonoFijoPersona("N/A");
        }
    }
}
