/* AssentSoftware (C)2023 */
package com.decathlon.logging.controller;

import com.decathlon.logging.audit.annotation.Auditable;
import com.decathlon.logging.dto.PersonDto;
import com.decathlon.logging.utils.ObjectsBuilderUtils;
import com.github.javafaker.Faker;

import jakarta.validation.Valid;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.zalando.logbook.spring.LogbookClientHttpRequestInterceptor;

import java.util.Locale;

@RestController
@RequestMapping("/api/v1/persons")
@Slf4j
public class SimpleController {

    private final Faker faker = new Faker(new Locale("es", "ES"));

    private final RestTemplate restTemplate;

    public SimpleController(
            RestTemplateBuilder builder, LogbookClientHttpRequestInterceptor interceptor) {
        this.restTemplate =
                builder.requestFactory(
                                () ->
                                        new BufferingClientHttpRequestFactory(
                                                new SimpleClientHttpRequestFactory()))
                        .additionalInterceptors(interceptor)
                        .basicAuthentication("Harold Da Costa", "secret")
                        .build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PersonDto save(
            @RequestBody @Valid PersonDto dto,
            @RequestParam("local_server_port") String localServerPort) {
        log.info("Entering in /api/v1/persons POST");

        HttpEntity<PersonDto> requestEntity = new HttpEntity<>(dto);

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromUriString(
                        "http://localhost:"
                                + localServerPort
                                + "/api/v1/persons/called-from-resttemplate");

        ResponseEntity<PersonDto> response =
                restTemplate.postForEntity(builder.toUriString(), requestEntity, PersonDto.class);

        return response.getBody();
    }

    @PostMapping("/called-from-resttemplate")
    @ResponseStatus(HttpStatus.CREATED)
    @Auditable(message = "Entering the calledFromRestTemplate controller")
    public PersonDto calledFromRestTemplate(@RequestBody @Valid PersonDto dto) {
        return dto;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PersonDto getPersonById(@PathVariable("id") Long id) {
        return ObjectsBuilderUtils.createFullPersonDto(faker);
    }
}
