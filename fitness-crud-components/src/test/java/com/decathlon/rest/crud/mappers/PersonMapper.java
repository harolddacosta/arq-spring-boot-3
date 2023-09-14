/* Decathlon (C)2023 */
package com.decathlon.rest.crud.mappers;

import com.decathlon.core.reflect.FieldConverter;
import com.decathlon.rest.crud.beans.PersonDto;
import com.decathlon.rest.crud.beans.PersonListDto;
import com.decathlon.rest.crud.entities.Persona;

public abstract class PersonMapper implements EntityMapper<PersonDto, PersonListDto, Persona> {

    public abstract Persona fromDtoToEntity(PersonDto personDto);

    public abstract Persona fromDtoToEntityForSave(PersonDto personDto);

    public abstract PersonDto fromEntityToDto(Persona persona);

    public abstract Persona mergeDtoIntoEntity(PersonDto personDto, Persona persona);

    public abstract Persona patchDtoIntoEntity(PersonDto personDto, Persona persona);

    public abstract PersonListDto fromEntityToListDto(Persona persona);

    public void doBeforeMapping(Persona persona, PersonDto personDto) {

        if (personDto.getSexId() != null) {
            persona.setSexo(null);
        }

        if (personDto.getPostalCodeId() != null) {
            persona.setCodigoPostal(null);
        }
    }

    public void doAfterMapping(Persona persona, PersonDto personDto) {
        persona.setNombreContactoUno(null);
        persona.setTelefonoContactoUno(null);
        persona.setParentescoContactoUno(null);

        persona.setNombreContactoDos(null);
        persona.setTelefonoContactoDos(null);
        persona.setParentescoContactoDos(null);

        if (persona.getSexo() != null
                && (persona.getSexo().getId() == null
                        || persona.getSexo()
                                .getId()
                                .equals(FieldConverter.getDeleteCodeForCombosValue()))) {
            persona.setSexo(null);
        }

        if (persona.getCodigoPostal() != null
                && (persona.getCodigoPostal().getId() == null
                        || persona.getCodigoPostal()
                                .getId()
                                .equals(FieldConverter.getDeleteCodeForCombosValue()))) {
            persona.setCodigoPostal(null);
        }
    }

    public void doAfterMapping(PersonDto personDto, Persona persona) {}
}
