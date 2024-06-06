/* AssentSoftware (C)2023 */
package com.decathlon.data.mappers;

import com.decathlon.data.domain.Persona;
import com.decathlon.data.dto.PersonDto;
import com.decathlon.data.mappers.decorators.PersonMapperDecorator;

import jakarta.annotation.Generated;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Generated(
        value = "org.mapstruct.ap.MappingProcessor",
        date = "2021-08-25T16:48:50+0200",
        comments =
                "version: 1.4.1.Final, compiler: javac, environment: Java 11.0.11 (Oracle Corporation)")
@Component
@Primary
public class PersonMapperImpl extends PersonMapperDecorator {

    @Autowired
    @Qualifier("delegate")
    private PersonMapper delegate;

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
