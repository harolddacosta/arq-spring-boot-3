/* Decathlon (C)2023 */
package com.decathlon.rest.crud.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.decathlon.core.reflect.FieldConverter;
import com.decathlon.rest.crud.TestRestServicesApplication;
import com.decathlon.rest.crud.beans.PersonDto;
import com.decathlon.rest.crud.entities.Persona;
import com.decathlon.rest.crud.mappers.PersonMapper;
import com.decathlon.rest.crud.repositories.PersonRepository;
import com.decathlon.rest.crud.utils.ObjectsBuilderUtils;
import com.decathlon.rest.data.domain.PageableRestResponse;
import com.github.javafaker.Faker;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.zalando.problem.DefaultProblem;
import org.zalando.problem.Status;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@SpringBootTest(
        classes = {TestRestServicesApplication.class},
        webEnvironment = WebEnvironment.RANDOM_PORT)
@Slf4j
class CrudRestControllerTest {

    private static final String PREFIX = "/api/v1/persons";

    @LocalServerPort private int serverPort;

    @Autowired private RestTemplate restTemplate;

    @SpyBean private PersonRepository personRepository;

    @Autowired private PersonMapper mapper;

    private Faker faker = new Faker(new Locale("es", "ES"));

    @Test
    void find_by_id_ok() {
        PersonDto personDto = ObjectsBuilderUtils.createFullPersonDto(faker);

        when(personRepository.findById(anyLong()))
                .thenReturn(Optional.of(mapper.fromDtoToEntity(personDto)));

        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("id", "100");

        ResponseEntity<PersonDto> savedPersonResponse =
                restTemplate.getForEntity(
                        buildUriComponent()
                                .path(PREFIX + "/{id}")
                                .buildAndExpand(pathParams)
                                .toUriString(),
                        PersonDto.class);

        assertThat(savedPersonResponse.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void find_by_id_not_present() {
        // PersonDto personDto = ObjectsBuilderUtils.createFullPersonDto(faker);

        when(personRepository.findById(anyLong())).thenReturn(Optional.empty());

        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("id", "100");

        try {
            restTemplate.getForEntity(
                    buildUriComponent()
                            .path(PREFIX + "/{id}")
                            .buildAndExpand(pathParams)
                            .toUriString(),
                    PersonDto.class);

        } catch (DefaultProblem e) {
            assertThat(e.getStatus()).isEqualTo(Status.NOT_FOUND);
        }
    }

    @Test
    void save_full_person() {
        // The assertions are in the saveFullPerson
        log.debug("************************");
        this.saveFullPerson();
    }

    @SuppressWarnings("unchecked")
    @Test
    void patch_person_validation_fail() throws Exception {
        PersonDto personDto = saveFullPerson();

        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("id", personDto.getId().toString());

        log.debug("*************************************************************************");

        try {
            PersonDto personPatchDto = new PersonDto();
            personPatchDto.setName(RandomStringUtils.randomAlphabetic(70));
            personDto.setName(personPatchDto.getName());
            patchAndDoComparison(pathParams, personDto, personPatchDto);
        } catch (DefaultProblem exception) {
            assertThat(
                            (ArrayList<LinkedHashMap<String, String>>)
                                    exception.getParameters().get("violations"))
                    .containsExactly(
                            new LinkedHashMap<>(
                                    Map.of(
                                            "field",
                                            "name",
                                            "message",
                                            "el tama\u00f1o debe estar entre 0 y 64")));
        }
        log.debug("*************************************************************************");
    }

    @Test
    void put_person_ok() throws Exception {
        PersonDto personDto = saveFullPerson();

        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("id", personDto.getId().toString());

        log.debug("*************************************************************************");

        PersonDto personPatchDto = SerializationUtils.clone(personDto);
        personPatchDto.setName(faker.name().firstName());
        personDto.setName(personPatchDto.getName());
        putAndDoComparison(pathParams, personDto, personPatchDto);

        log.debug("*************************************************************************");
    }

    @Test
    void patch_person_ok() throws Exception {
        PersonDto personDto = saveFullPerson();

        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("id", personDto.getId().toString());

        log.debug("*************************************************************************");

        PersonDto personPatchDto = new PersonDto();
        personPatchDto.setId(-123L);
        personPatchDto.setCode("no cambiar");
        patchAndDoComparison(pathParams, personDto, personPatchDto);

        log.debug("*************************************************************************");

        personPatchDto = new PersonDto();
        personPatchDto.setName(faker.name().firstName());
        personDto.setName(personPatchDto.getName());
        patchAndDoComparison(pathParams, personDto, personPatchDto);

        log.debug("*************************************************************************");

        personPatchDto = new PersonDto();
        personPatchDto.setLastName(faker.name().lastName() + " " + faker.name().lastName());
        personDto.setLastName(personPatchDto.getLastName());
        patchAndDoComparison(pathParams, personDto, personPatchDto);

        log.debug("*************************************************************************");

        personPatchDto = new PersonDto();
        personPatchDto.setIdentityDocument(
                RandomStringUtils.randomAlphanumeric(1)
                        + "-"
                        + RandomStringUtils.randomNumeric(14));
        personDto.setIdentityDocument(personPatchDto.getIdentityDocument());
        patchAndDoComparison(pathParams, personDto, personPatchDto);

        log.debug("*************************************************************************");

        personPatchDto = new PersonDto();
        personPatchDto.setSanitaryDocument(
                RandomStringUtils.randomAlphanumeric(1)
                        + "-"
                        + RandomStringUtils.randomNumeric(14));
        personDto.setSanitaryDocument(personPatchDto.getSanitaryDocument());
        patchAndDoComparison(pathParams, personDto, personPatchDto);

        log.debug("*************************************************************************");

        personPatchDto = new PersonDto();
        personPatchDto.setMobilePhone(faker.phoneNumber().cellPhone());
        personDto.setMobilePhone(personPatchDto.getMobilePhone());
        patchAndDoComparison(pathParams, personDto, personPatchDto);

        log.debug("*************************************************************************");

        personPatchDto = new PersonDto();
        personPatchDto.setHomePhone(faker.phoneNumber().phoneNumber());
        personDto.setHomePhone(personPatchDto.getHomePhone());
        patchAndDoComparison(pathParams, personDto, personPatchDto);

        log.debug("*************************************************************************");

        personPatchDto = new PersonDto();
        personPatchDto.setEmail(faker.internet().emailAddress());
        personDto.setEmail(personPatchDto.getEmail());
        patchAndDoComparison(pathParams, personDto, personPatchDto);

        log.debug("*************************************************************************");

        personPatchDto = new PersonDto();
        personPatchDto.setAddressLine1(faker.address().fullAddress());
        personPatchDto.setAddressLine2(faker.address().secondaryAddress());
        personDto.setAddressLine1(personPatchDto.getAddressLine1());
        personDto.setAddressLine2(personPatchDto.getAddressLine2());
        patchAndDoComparison(pathParams, personDto, personPatchDto);

        log.debug("*************************************************************************");

        personPatchDto = new PersonDto();
        personPatchDto.setPostalCodeId(100L);
        personDto.setPostalCodeId(personPatchDto.getPostalCodeId());
        patchAndDoComparison(pathParams, personDto, personPatchDto);

        log.debug("*************************************************************************");

        personPatchDto = new PersonDto();
        LocalDate birthDate = new java.sql.Date(faker.date().birthday().getTime()).toLocalDate();
        personPatchDto.setBirthDate(birthDate);
        personDto.setBirthDate(personPatchDto.getBirthDate());
        patchAndDoComparison(pathParams, personDto, personPatchDto);

        log.debug("*************************************************************************");

        personPatchDto = new PersonDto();
        personPatchDto.setAge((float) Period.between(birthDate, LocalDate.now()).getYears());
        personDto.setAge(personPatchDto.getAge());
        patchAndDoComparison(pathParams, personDto, personPatchDto);

        log.debug("*************************************************************************");

        personPatchDto = new PersonDto();
        personPatchDto.setUrl1FileName(faker.file().fileName());
        personDto.setUrl1FileName(personPatchDto.getUrl1FileName());
        patchAndDoComparison(pathParams, personDto, personPatchDto);

        log.debug("*************************************************************************");

        personPatchDto = new PersonDto();
        personPatchDto.setSexId(faker.random().nextInt(1, 2).longValue());
        personDto.setSexId(personPatchDto.getSexId());
        patchAndDoComparison(pathParams, personDto, personPatchDto);
    }

    @Test
    void patch_person_nullify_ok() throws Exception {
        PersonDto personDto = saveFullPerson();

        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("id", personDto.getId().toString());

        log.debug("*************************************************************************");

        PersonDto personPatchDto = new PersonDto();
        personPatchDto.setId(null);
        personPatchDto.setCode(null);
        patchAndDoComparison(pathParams, personDto, personPatchDto);

        personPatchDto = new PersonDto();
        personPatchDto.setId(FieldConverter.getDeleteCodeForLong());
        personPatchDto.setCode(FieldConverter.getDeleteCodeForString());
        patchAndDoComparison(pathParams, personDto, personPatchDto);

        log.debug("*************************************************************************");

        personPatchDto = new PersonDto();
        personPatchDto.setSanitaryDocument(null);
        patchAndDoComparison(pathParams, personDto, personPatchDto);

        personPatchDto = new PersonDto();
        personPatchDto.setSanitaryDocument(FieldConverter.getDeleteCodeForString());
        personDto.setSanitaryDocument(null);
        patchAndDoComparison(pathParams, personDto, personPatchDto);

        log.debug("*************************************************************************");

        personPatchDto = new PersonDto();
        personPatchDto.setHomePhone(null);
        patchAndDoComparison(pathParams, personDto, personPatchDto);

        personPatchDto = new PersonDto();
        personPatchDto.setHomePhone(FieldConverter.getDeleteCodeForString());
        personDto.setHomePhone("N/A");
        patchAndDoComparison(pathParams, personDto, personPatchDto);

        log.debug("*************************************************************************");

        personPatchDto = new PersonDto();
        personPatchDto.setEmail(null);
        patchAndDoComparison(pathParams, personDto, personPatchDto);

        personPatchDto = new PersonDto();
        personPatchDto.setEmail(FieldConverter.getDeleteCodeForString());
        personDto.setEmail(null);
        patchAndDoComparison(pathParams, personDto, personPatchDto);

        log.debug("*************************************************************************");

        personPatchDto = new PersonDto();
        personPatchDto.setAddressLine2(null);
        patchAndDoComparison(pathParams, personDto, personPatchDto);

        personPatchDto = new PersonDto();
        personPatchDto.setAddressLine2(FieldConverter.getDeleteCodeForString());
        personDto.setAddressLine2(null);
        patchAndDoComparison(pathParams, personDto, personPatchDto);

        log.debug("*************************************************************************");

        personPatchDto = new PersonDto();
        personPatchDto.setBirthDate(null);
        patchAndDoComparison(pathParams, personDto, personPatchDto);

        personPatchDto = new PersonDto();
        personPatchDto.setBirthDate(FieldConverter.getDeleteCodeForLocalDate());
        personDto.setBirthDate(null);
        patchAndDoComparison(pathParams, personDto, personPatchDto);

        log.debug("*************************************************************************");

        personPatchDto = new PersonDto();
        personPatchDto.setUrl1FileName(null);
        patchAndDoComparison(pathParams, personDto, personPatchDto);

        personPatchDto = new PersonDto();
        personPatchDto.setUrl1FileName(FieldConverter.getDeleteCodeForString());
        personDto.setUrl1FileName(null);
        patchAndDoComparison(pathParams, personDto, personPatchDto);

        log.debug("*************************************************************************");

        personPatchDto = new PersonDto();
        personPatchDto.setSexId(null);
        patchAndDoComparison(pathParams, personDto, personPatchDto);

        personPatchDto = new PersonDto();
        personPatchDto.setSexId(FieldConverter.getDeleteCodeForCombosValue());
        personDto.setSexId(null);
        patchAndDoComparison(pathParams, personDto, personPatchDto);

        log.debug("*************************************************************************");
    }

    @SuppressWarnings("unchecked")
    @Test
    void post_dto_validations() {
        PersonDto personDto = ObjectsBuilderUtils.createFullPersonDto(faker);
        personDto.setBirthDate(
                new java.sql.Date(faker.date().future(10, TimeUnit.DAYS).getTime()).toLocalDate());
        try {
            restTemplate.exchange(
                    buildUriComponent().path(PREFIX).build().toUriString(),
                    HttpMethod.POST,
                    new HttpEntity<>(personDto),
                    PersonDto.class);
            assertThat(false).isTrue();
        } catch (DefaultProblem exception) {
            assertThat(
                            (ArrayList<LinkedHashMap<String, String>>)
                                    exception.getParameters().get("violations"))
                    .containsExactly(
                            new LinkedHashMap<>(
                                    Map.of(
                                            "field",
                                            "birthDate",
                                            "message",
                                            "debe ser una fecha pasada")));
        }

        personDto = ObjectsBuilderUtils.createFullPersonDto(faker);
        personDto.setAge(-200F);
        try {
            restTemplate.exchange(
                    buildUriComponent().path(PREFIX).build().toUriString(),
                    HttpMethod.POST,
                    new HttpEntity<>(personDto),
                    PersonDto.class);
            assertThat(false).isTrue();
        } catch (DefaultProblem exception) {
            assertThat(
                            (ArrayList<LinkedHashMap<String, String>>)
                                    exception.getParameters().get("violations"))
                    .containsExactly(
                            new LinkedHashMap<>(
                                    Map.of(
                                            "field",
                                            "age",
                                            "message",
                                            "debe ser mayor que o igual a 0")));
        }
    }

    @Test
    void delete_ok() {
        PersonDto personDto = ObjectsBuilderUtils.createFullPersonDto(faker);

        when(personRepository.findById(anyLong()))
                .thenReturn(Optional.of(mapper.fromDtoToEntity(personDto)));

        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("id", "100");

        ResponseEntity<PersonDto> savedPersonResponse =
                restTemplate.exchange(
                        buildUriComponent()
                                .path(PREFIX + "/{id}")
                                .buildAndExpand(pathParams)
                                .toUriString(),
                        HttpMethod.DELETE,
                        null,
                        PersonDto.class);

        assertThat(savedPersonResponse.getStatusCode().value())
                .isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void find_all_pagination() {
        when(personRepository.findPersonsLike(any(), any()))
                .thenReturn(new PageImpl<Persona>(List.of(new Persona())));

        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("id", "100");

        @SuppressWarnings("rawtypes")
        ResponseEntity<PageableRestResponse> savedPersonResponse =
                restTemplate.getForEntity(
                        buildUriComponent()
                                .path(PREFIX + "/paginated")
                                .buildAndExpand(pathParams)
                                .toUriString(),
                        PageableRestResponse.class);

        assertThat(savedPersonResponse.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
    }

    private PersonDto saveFullPerson() {
        return saveAndAssertPerson(ObjectsBuilderUtils.createFullPersonDto(faker));
    }

    private PersonDto saveAndAssertPerson(PersonDto personDto) {
        ResponseEntity<PersonDto> savedPersonResponse =
                restTemplate.exchange(
                        buildUriComponent().path(PREFIX).build().toUriString(),
                        HttpMethod.POST,
                        new HttpEntity<>(personDto),
                        PersonDto.class);

        assertThat(savedPersonResponse.getStatusCode().value())
                .isEqualTo(HttpStatus.CREATED.value());

        personDto.setId(savedPersonResponse.getBody().getId());
        personDto.setCode(savedPersonResponse.getBody().getCode());
        personDto.setHomePhone(savedPersonResponse.getBody().getHomePhone());
        personDto.setMunicipalityId(savedPersonResponse.getBody().getMunicipalityId());
        personDto.setProvinceId(savedPersonResponse.getBody().getProvinceId());
        personDto.setVersion(savedPersonResponse.getBody().getVersion());

        log.debug("Person created by Dto		:{}", personDto);
        log.debug("Person saved from response	:{}", savedPersonResponse.getBody());

        assertThat(EqualsBuilder.reflectionEquals(savedPersonResponse.getBody(), personDto, false))
                .isTrue();

        return savedPersonResponse.getBody();
    }

    private void patchAndDoComparison(
            Map<String, String> pathParams, PersonDto personDto, PersonDto personToPatchDto) {
        when(personRepository.findById(anyLong()))
                .thenReturn(Optional.of(mapper.fromDtoToEntity(personDto)));

        personToPatchDto =
                restTemplate
                        .exchange(
                                buildUriComponent()
                                        .path(PREFIX + "/{id}")
                                        .buildAndExpand(pathParams)
                                        .toUriString(),
                                HttpMethod.PATCH,
                                new HttpEntity<>(personToPatchDto),
                                PersonDto.class)
                        .getBody();

        personDto.setVersion(personToPatchDto.getVersion());

        log.debug("Person in screen     :{}", personDto);
        log.debug("Person coming in rest:{}", personToPatchDto);

        assertThat(EqualsBuilder.reflectionEquals(personToPatchDto, personDto, false)).isTrue();
    }

    private void putAndDoComparison(
            Map<String, String> pathParams, PersonDto personDto, PersonDto personToPatchDto) {
        when(personRepository.findById(anyLong()))
                .thenReturn(Optional.of(mapper.fromDtoToEntity(personDto)));

        personToPatchDto =
                restTemplate
                        .exchange(
                                buildUriComponent()
                                        .path(PREFIX + "/{id}")
                                        .buildAndExpand(pathParams)
                                        .toUriString(),
                                HttpMethod.PUT,
                                new HttpEntity<>(personToPatchDto),
                                PersonDto.class)
                        .getBody();

        personDto.setVersion(personToPatchDto.getVersion());

        log.debug("Person in screen     :{}", personDto);
        log.debug("Person coming in rest:{}", personToPatchDto);

        assertThat(EqualsBuilder.reflectionEquals(personToPatchDto, personDto, false)).isTrue();
    }

    private UriComponentsBuilder buildUriComponent() {
        return UriComponentsBuilder.newInstance().scheme("http").host("localhost").port(serverPort);
    }
}
