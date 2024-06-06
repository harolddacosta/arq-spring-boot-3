/* AssentSoftware (C)2023 */
package com.decathlon.data.mappers.decorators;

import com.decathlon.data.domain.Persona;
import com.decathlon.data.dto.PersonDto;
import com.decathlon.data.mappers.PersonMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class PersonMapperDecorator extends PersonMapper {

    @Autowired
    @Qualifier("delegate")
    private PersonMapper delegate;

    @Override
    public Persona patchDtoIntoEntity(PersonDto personDto, Persona persona) {
        delegate.patchDtoIntoEntity(personDto, persona);

        return persona;
    }
}
