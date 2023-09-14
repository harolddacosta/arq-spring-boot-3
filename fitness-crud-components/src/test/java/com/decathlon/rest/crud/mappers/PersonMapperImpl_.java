/* Decathlon (C)2023 */
package com.decathlon.rest.crud.mappers;

import com.decathlon.rest.crud.beans.PersonDto;
import com.decathlon.rest.crud.beans.PersonListDto;
import com.decathlon.rest.crud.entities.CodigoPostal;
import com.decathlon.rest.crud.entities.Persona;
import com.decathlon.rest.crud.entities.Sexo;

import jakarta.annotation.Generated;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Generated(
        value = "org.mapstruct.ap.MappingProcessor",
        date = "2021-12-13T17:53:40+0100",
        comments =
                "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.12 (Oracle Corporation)")
@Component
@Qualifier("delegate")
public class PersonMapperImpl_ extends PersonMapper {

    @Override
    public List<PersonListDto> entitiesToDtos(List<Persona> content) {
        if (content == null) {
            return null;
        }

        List<PersonListDto> list = new ArrayList<PersonListDto>(content.size());
        for (Persona persona : content) {
            list.add(fromEntityToListDto(persona));
        }

        return list;
    }

    @Override
    public Persona fromDtoToEntity(PersonDto personDto) {
        if (personDto == null) {
            return null;
        }

        Persona persona = new Persona();

        doBeforeMapping(persona, personDto);

        persona.setCodigoPostal(personDtoToCodigoPostal(personDto));
        persona.setSexo(personDtoToSexo(personDto));
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

        persona.setCodigoPostal(personDtoToCodigoPostal1(personDto));
        persona.setSexo(personDtoToSexo1(personDto));
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
        personDto.setPostalCodeId(personaCodigoPostalId(persona));
        personDto.setSexId(personaSexoId(persona));
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

        if (persona.getCodigoPostal() == null) {
            persona.setCodigoPostal(new CodigoPostal());
        }
        personDtoToCodigoPostal2(personDto, persona.getCodigoPostal());
        if (persona.getSexo() == null) {
            persona.setSexo(new Sexo());
        }
        personDtoToSexo2(personDto, persona.getSexo());
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

        if (persona.getSexo() == null) {
            persona.setSexo(new Sexo());
        }
        personDtoToSexo3(personDto, persona.getSexo());
        if (persona.getCodigoPostal() == null) {
            persona.setCodigoPostal(new CodigoPostal());
        }
        personDtoToCodigoPostal3(personDto, persona.getCodigoPostal());
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

    @Override
    public PersonListDto fromEntityToListDto(Persona persona) {
        if (persona == null) {
            return null;
        }

        PersonListDto personListDto = new PersonListDto();

        personListDto.setName(persona.getNombrePersona());
        personListDto.setLastName(persona.getApellidoPersona());
        personListDto.setId(persona.getId());

        return personListDto;
    }

    protected CodigoPostal personDtoToCodigoPostal(PersonDto personDto) {
        if (personDto == null) {
            return null;
        }

        CodigoPostal codigoPostal = new CodigoPostal();

        codigoPostal.setId(personDto.getPostalCodeId());

        return codigoPostal;
    }

    protected Sexo personDtoToSexo(PersonDto personDto) {
        if (personDto == null) {
            return null;
        }

        Sexo sexo = new Sexo();

        sexo.setId(personDto.getSexId());

        return sexo;
    }

    protected CodigoPostal personDtoToCodigoPostal1(PersonDto personDto) {
        if (personDto == null) {
            return null;
        }

        CodigoPostal codigoPostal = new CodigoPostal();

        codigoPostal.setId(personDto.getPostalCodeId());

        return codigoPostal;
    }

    protected Sexo personDtoToSexo1(PersonDto personDto) {
        if (personDto == null) {
            return null;
        }

        Sexo sexo = new Sexo();

        sexo.setId(personDto.getSexId());

        return sexo;
    }

    private Long personaCodigoPostalId(Persona persona) {
        if (persona == null) {
            return null;
        }
        CodigoPostal codigoPostal = persona.getCodigoPostal();
        if (codigoPostal == null) {
            return null;
        }
        Long id = codigoPostal.getId();
        if (id == null) {
            return null;
        }
        return id;
    }

    private Long personaSexoId(Persona persona) {
        if (persona == null) {
            return null;
        }
        Sexo sexo = persona.getSexo();
        if (sexo == null) {
            return null;
        }
        Long id = sexo.getId();
        if (id == null) {
            return null;
        }
        return id;
    }

    protected void personDtoToCodigoPostal2(PersonDto personDto, CodigoPostal mappingTarget) {
        if (personDto == null) {
            return;
        }

        mappingTarget.setId(personDto.getPostalCodeId());
    }

    protected void personDtoToSexo2(PersonDto personDto, Sexo mappingTarget) {
        if (personDto == null) {
            return;
        }

        mappingTarget.setId(personDto.getSexId());
    }

    protected void personDtoToSexo3(PersonDto personDto, Sexo mappingTarget) {
        if (personDto == null) {
            return;
        }

        if (personDto.getSexId() != null) {
            mappingTarget.setId(personDto.getSexId());
        }
    }

    protected void personDtoToCodigoPostal3(PersonDto personDto, CodigoPostal mappingTarget) {
        if (personDto == null) {
            return;
        }

        if (personDto.getPostalCodeId() != null) {
            mappingTarget.setId(personDto.getPostalCodeId());
        }
    }
}
