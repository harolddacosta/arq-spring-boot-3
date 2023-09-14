/* Decathlon (C)2023 */
package com.decathlon.rest.crud.mappers;

import com.decathlon.rest.crud.beans.PersonDto;
import com.decathlon.rest.crud.beans.PersonListDto;
import com.decathlon.rest.crud.entities.Persona;
import com.decathlon.rest.crud.mappers.decorators.PersonMapperDecorator;

import jakarta.annotation.Generated;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;

@Generated(
        value = "org.mapstruct.ap.MappingProcessor",
        date = "2021-12-13T17:53:40+0100",
        comments =
                "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.12 (Oracle Corporation)")
@Component
@Primary
public class PersonMapperImpl extends PersonMapperDecorator {

    @Autowired
    @Qualifier("delegate")
    private PersonMapper delegate;

    @Override
    public List<PersonListDto> entitiesToDtos(List<Persona> content) {
        return delegate.entitiesToDtos(content);
    }

    @Override
    public Persona fromDtoToEntity(PersonDto personDto) {
        return delegate.fromDtoToEntity(personDto);
    }

    @Override
    public Persona fromDtoToEntityForSave(PersonDto personDto) {
        return delegate.fromDtoToEntityForSave(personDto);
    }

    @Override
    public PersonDto fromEntityToDto(Persona persona) {
        return delegate.fromEntityToDto(persona);
    }

    @Override
    public Persona mergeDtoIntoEntity(PersonDto personDto, Persona persona) {
        return delegate.mergeDtoIntoEntity(personDto, persona);
    }

    @Override
    public PersonListDto fromEntityToListDto(Persona persona) {
        return delegate.fromEntityToListDto(persona);
    }

    @Override
    public void doBeforeMapping(Persona persona, PersonDto personDto) {
        delegate.doBeforeMapping(persona, personDto);
    }

    @Override
    public void doAfterMapping(Persona persona, PersonDto personDto) {
        delegate.doAfterMapping(persona, personDto);
    }

    @Override
    public void doAfterMapping(PersonDto personDto, Persona persona) {
        delegate.doAfterMapping(personDto, persona);
    }
}
