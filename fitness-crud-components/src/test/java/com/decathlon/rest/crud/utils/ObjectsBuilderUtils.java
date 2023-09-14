/* Decathlon (C)2023 */
package com.decathlon.rest.crud.utils;

import com.decathlon.rest.crud.beans.PersonDto;
import com.github.javafaker.Faker;

import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDate;
import java.time.Period;

public class ObjectsBuilderUtils {

    private ObjectsBuilderUtils() {
        // Utility class
    }

    public static PersonDto createFullPersonDto(Faker faker) {
        PersonDto personDto = new PersonDto();
        personDto.setName(faker.name().firstName());
        personDto.setLastName(faker.name().lastName() + " " + faker.name().lastName());
        personDto.setIdentityDocument(
                RandomStringUtils.randomAlphanumeric(1)
                        + "-"
                        + RandomStringUtils.randomNumeric(14));
        personDto.setSanitaryDocument(
                RandomStringUtils.randomAlphanumeric(1)
                        + "-"
                        + RandomStringUtils.randomNumeric(14));
        personDto.setMobilePhone(faker.phoneNumber().cellPhone());
        personDto.setHomePhone(faker.phoneNumber().phoneNumber());
        personDto.setEmail(faker.internet().emailAddress());
        personDto.setAddressLine1(faker.address().fullAddress());
        personDto.setAddressLine2(faker.address().secondaryAddress());
        personDto.setPostalCodeId(faker.random().nextInt(1, 14665).longValue()); // 1476L
        LocalDate birthDate = new java.sql.Date(faker.date().birthday().getTime()).toLocalDate();
        personDto.setBirthDate(birthDate);
        personDto.setAge((float) Period.between(birthDate, LocalDate.now()).getYears());
        personDto.setUrl1FileName(faker.file().fileName());
        personDto.setSexId(faker.random().nextInt(1, 2).longValue());

        return personDto;
    }
}
