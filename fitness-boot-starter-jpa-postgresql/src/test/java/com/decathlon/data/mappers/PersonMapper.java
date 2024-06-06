/* AssentSoftware (C)2023 */
package com.decathlon.data.mappers;

import com.decathlon.data.domain.Persona;
import com.decathlon.data.dto.PersonDto;

public abstract class PersonMapper {

    public abstract Persona fromDtoToEntity(PersonDto personDto);

    public abstract Persona fromDtoToEntityForSave(PersonDto personDto);

    public abstract PersonDto fromEntityToDto(Persona persona);

    public abstract Persona mergeDtoIntoEntity(PersonDto personDto, Persona persona);

    public abstract Persona patchDtoIntoEntity(PersonDto personDto, Persona persona);

    public void doBeforeMapping(Persona persona, PersonDto personDto) {}

    public void doAfterMapping(Persona persona, PersonDto personDto) {
        persona.setNombreContactoUno(null);
        persona.setTelefonoContactoUno(null);
        persona.setParentescoContactoUno(null);

        persona.setNombreContactoDos(null);
        persona.setTelefonoContactoDos(null);
        persona.setParentescoContactoDos(null);
    }

    public void doAfterMapping(PersonDto personDto, Persona persona) {}
}
