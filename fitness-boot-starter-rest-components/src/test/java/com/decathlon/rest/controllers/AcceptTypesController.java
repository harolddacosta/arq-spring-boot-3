/* AssentSoftware (C)2023 */
package com.decathlon.rest.controllers;

import com.decathlon.rest.dtos.DatesDto;
import com.decathlon.rest.utils.RequestUtils;
import com.decathlon.rest.utils.dtos.KeyValueXmlResponseDto;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/types-handler")
@Slf4j
public class AcceptTypesController {

    @GetMapping
    public ResponseEntity<DatesDto> getDatesInJson(
            @RequestParam(value = "add_nullable_value", required = false, defaultValue = "false")
                    Boolean addNullableValue) {
        DatesDto demoDto = new DatesDto();

        if (addNullableValue) {
            demoDto.setName("Harold");
        }

        return new ResponseEntity<>(demoDto, HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getTextPlainContent() {
        return new ResponseEntity<>("Prueba de string", HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<KeyValueXmlResponseDto> getXmlContent() {
        return new ResponseEntity<>(new KeyValueXmlResponseDto("Prueba"), HttpStatus.OK);
    }

    @PostMapping(value = "/from-spa")
    public ResponseEntity<DatesDto> getDatesInJsonWithString(@RequestBody DatesDto datesDto) {
        log.debug("dto:{}", datesDto);

        MultiValueMap<String, String> headers = RequestUtils.buildResourceCreationHeaders(1L);

        return new ResponseEntity<>(datesDto, headers, HttpStatus.OK);
    }
}
