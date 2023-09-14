/* Decathlon (C)2023 */
package com.decathlon.rest.crud.controllers;

import com.decathlon.rest.crud.beans.PersonDto;
import com.decathlon.rest.crud.beans.PersonListDto;
import com.decathlon.rest.crud.entities.Persona;
import com.decathlon.rest.crud.mappers.PersonMapper;
import com.decathlon.rest.crud.service.PersonService;

import jakarta.validation.Validator;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/persons")
public class PersonRestController extends CrudRestController<PersonDto, PersonListDto, Persona> {

    public PersonRestController(
            PersonService personService, PersonMapper personMapper, Validator validator) {
        super(personService, personMapper, validator);
    }
}
