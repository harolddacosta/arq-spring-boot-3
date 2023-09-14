/* Decathlon (C)2023 */
package com.decathlon.rest.crud.mappers.decorators;

import com.decathlon.core.reflect.FieldConverter;
import com.decathlon.rest.crud.beans.PersonDto;
import com.decathlon.rest.crud.entities.Persona;
import com.decathlon.rest.crud.mappers.PersonMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class PersonMapperDecorator extends PersonMapper {

    @Autowired
    @Qualifier("delegate")
    private PersonMapper delegate;

    private FieldConverter fieldConverter = new FieldConverter();

    @Override
    public Persona patchDtoIntoEntity(PersonDto personDto, Persona persona) {
        delegate.patchDtoIntoEntity(personDto, persona);

        //		FieldConverter fieldConverter = new FieldConverter();
        fieldConverter.replace(persona);

        return persona;
    }
}
