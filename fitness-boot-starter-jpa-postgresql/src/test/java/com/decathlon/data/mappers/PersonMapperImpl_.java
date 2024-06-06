/* AssentSoftware (C)2023 */
package com.decathlon.data.mappers;

import com.decathlon.data.domain.Persona;
import com.decathlon.data.dto.PersonDto;

import jakarta.annotation.Generated;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Generated(
        value = "org.mapstruct.ap.MappingProcessor",
        date = "2021-08-25T16:48:49+0200",
        comments =
                "version: 1.4.1.Final, compiler: javac, environment: Java 11.0.11 (Oracle Corporation)")
@Component
@Qualifier("delegate")
public class PersonMapperImpl_ extends PersonMapper {

    @Override
    public Persona fromDtoToEntity(PersonDto personDto) {
        if (personDto == null) {
            return null;
        }

        Persona persona = new Persona();

        doBeforeMapping(persona, personDto);

        persona.setCodigoPersona(personDto.getCode());
        persona.setNombrePersona(personDto.getName());
        persona.setApellidoPersona(personDto.getLastName());
        persona.setCedulaPersona(personDto.getIdentityDocument());
        persona.setNumeroMsas(personDto.getSanitaryDocument());
        persona.setTelefonoMovilPersona(personDto.getMobilePhone());
        persona.setTelefonoFijoPersona(personDto.getHomePhone());
        persona.setEMailPersona(personDto.getEmail());
        persona.setDireccionPersona(personDto.getAddressLine1());
        persona.setDireccionPersonaCalleAvenida(personDto.getAddressLine2());
        persona.setFechaNacimientoPersona(personDto.getBirthDate());
        persona.setEdad(personDto.getAge());
        persona.setId(personDto.getId());
        persona.setVersion(personDto.getVersion());
        persona.setUrl1FileName(personDto.getUrl1FileName());

        doAfterMapping(persona, personDto);

        return persona;
    }

    @Override
    public Persona fromDtoToEntityForSave(PersonDto personDto) {
        if (personDto == null) {
            return null;
        }

        Persona persona = new Persona();

        doBeforeMapping(persona, personDto);

        persona.setNombrePersona(personDto.getName());
        persona.setApellidoPersona(personDto.getLastName());
        persona.setCedulaPersona(personDto.getIdentityDocument());
        persona.setNumeroMsas(personDto.getSanitaryDocument());
        persona.setTelefonoMovilPersona(personDto.getMobilePhone());
        persona.setTelefonoFijoPersona(personDto.getHomePhone());
        persona.setEMailPersona(personDto.getEmail());
        persona.setDireccionPersona(personDto.getAddressLine1());
        persona.setDireccionPersonaCalleAvenida(personDto.getAddressLine2());
        persona.setFechaNacimientoPersona(personDto.getBirthDate());
        persona.setEdad(personDto.getAge());
        persona.setUrl1FileName(personDto.getUrl1FileName());

        doAfterMapping(persona, personDto);

        return persona;
    }

    @Override
    public PersonDto fromEntityToDto(Persona persona) {
        if (persona == null) {
            return null;
        }

        PersonDto personDto = new PersonDto();

        personDto.setCode(persona.getCodigoPersona());
        personDto.setName(persona.getNombrePersona());
        personDto.setLastName(persona.getApellidoPersona());
        personDto.setIdentityDocument(persona.getCedulaPersona());
        personDto.setSanitaryDocument(persona.getNumeroMsas());
        personDto.setMobilePhone(persona.getTelefonoMovilPersona());
        personDto.setHomePhone(persona.getTelefonoFijoPersona());
        personDto.setEmail(persona.getEMailPersona());
        personDto.setAddressLine1(persona.getDireccionPersona());
        personDto.setAddressLine2(persona.getDireccionPersonaCalleAvenida());
        personDto.setBirthDate(persona.getFechaNacimientoPersona());
        personDto.setAge(persona.getEdad());
        personDto.setId(persona.getId());
        personDto.setUrl1FileName(persona.getUrl1FileName());
        personDto.setVersion(persona.getVersion());

        doAfterMapping(personDto, persona);

        return personDto;
    }

    @Override
    public Persona mergeDtoIntoEntity(PersonDto personDto, Persona persona) {
        if (personDto == null) {
            return null;
        }

        doBeforeMapping(persona, personDto);

        persona.setNombrePersona(personDto.getName());
        persona.setApellidoPersona(personDto.getLastName());
        persona.setCedulaPersona(personDto.getIdentityDocument());
        persona.setNumeroMsas(personDto.getSanitaryDocument());
        persona.setTelefonoMovilPersona(personDto.getMobilePhone());
        persona.setTelefonoFijoPersona(personDto.getHomePhone());
        persona.setEMailPersona(personDto.getEmail());
        persona.setDireccionPersona(personDto.getAddressLine1());
        persona.setDireccionPersonaCalleAvenida(personDto.getAddressLine2());
        persona.setFechaNacimientoPersona(personDto.getBirthDate());
        persona.setEdad(personDto.getAge());
        persona.setVersion(personDto.getVersion());
        persona.setUrl1FileName(personDto.getUrl1FileName());

        doAfterMapping(persona, personDto);

        return persona;
    }

    @Override
    public Persona patchDtoIntoEntity(PersonDto personDto, Persona persona) {
        if (personDto == null) {
            return null;
        }

        doBeforeMapping(persona, personDto);

        if (personDto.getName() != null) {
            persona.setNombrePersona(personDto.getName());
        }
        if (personDto.getLastName() != null) {
            persona.setApellidoPersona(personDto.getLastName());
        }
        if (personDto.getIdentityDocument() != null) {
            persona.setCedulaPersona(personDto.getIdentityDocument());
        }
        if (personDto.getSanitaryDocument() != null) {
            persona.setNumeroMsas(personDto.getSanitaryDocument());
        }
        if (personDto.getMobilePhone() != null) {
            persona.setTelefonoMovilPersona(personDto.getMobilePhone());
        }
        if (personDto.getHomePhone() != null) {
            persona.setTelefonoFijoPersona(personDto.getHomePhone());
        }
        if (personDto.getEmail() != null) {
            persona.setEMailPersona(personDto.getEmail());
        }
        if (personDto.getAddressLine1() != null) {
            persona.setDireccionPersona(personDto.getAddressLine1());
        }
        if (personDto.getAddressLine2() != null) {
            persona.setDireccionPersonaCalleAvenida(personDto.getAddressLine2());
        }
        if (personDto.getBirthDate() != null) {
            persona.setFechaNacimientoPersona(personDto.getBirthDate());
        }
        if (personDto.getAge() != null) {
            persona.setEdad(personDto.getAge());
        }
        persona.setVersion(personDto.getVersion());
        if (personDto.getUrl1FileName() != null) {
            persona.setUrl1FileName(personDto.getUrl1FileName());
        }

        doAfterMapping(persona, personDto);

        return persona;
    }
}
