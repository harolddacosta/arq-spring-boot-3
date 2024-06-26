/* AssentSoftware (C)2023 */
package com.decathlon.data.manager.impl;

import com.decathlon.data.domain.Persona;
import com.decathlon.data.manager.PersonService;
import com.decathlon.data.repositories.PersonRepository;

import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    @Override
    @Transactional
    public Persona save(Persona entityToSave) {
        entityToSave.setCodigoPersona(UUID.randomUUID().toString().substring(0, 16));

        return personRepository.save(entityToSave);
    }

    @Override
    @Transactional
    public Persona saveCodeNotAutoGenerated(Persona entityToSave) {
        entityToSave.setCodigoPersona("same code");

        return personRepository.save(entityToSave);
    }

    @Override
    public Persona getById(Long id) {
        return personRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }
}
