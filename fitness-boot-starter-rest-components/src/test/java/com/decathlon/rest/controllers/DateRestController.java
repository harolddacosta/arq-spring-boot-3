/* AssentSoftware (C)2023 */
package com.decathlon.rest.controllers;

import com.decathlon.rest.dtos.DatesDto;
import com.decathlon.rest.utils.DateUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

@SuppressWarnings("deprecation")
@RestController
@RequestMapping("/api/v1/dates-handler")
@RequiredArgsConstructor
@Slf4j
public class DateRestController {

    private final DateUtils dateUtils;

    @GetMapping
    public ResponseEntity<DatesDto> getDatesInJson(
            @RequestParam(value = "add_nullable_value", required = false, defaultValue = "false")
                    Boolean addNullableValue) {
        DatesDto dateDto = new DatesDto();
        dateDto.setLocalDateField(LocalDate.of(2020, 10, 21));
        dateDto.setLocalDateTimeField(LocalDateTime.of(2020, 10, 21, 10, 00, 50));
        dateDto.setOldDateField(Date.from(Instant.parse("2020-10-21T10:00:50.000Z")));

        if (addNullableValue) {
            dateDto.setName("Harold");
        }

        return new ResponseEntity<>(dateDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DatesDto> getDatesInJsonWithDto(@RequestBody DatesDto datesDto) {
        return new ResponseEntity<>(datesDto, HttpStatus.OK);
    }

    @GetMapping("/localdate")
    public ResponseEntity<DatesDto> localDate(@RequestParam("date") LocalDate localDate) {
        log.debug("localDate:{}", localDate);

        DatesDto demoDto = new DatesDto();
        demoDto.setLocalDateField(localDate);

        return new ResponseEntity<>(demoDto, HttpStatus.OK);
    }

    @GetMapping("/localdatetime")
    public ResponseEntity<DatesDto> dateTime(@RequestParam("date") LocalDateTime localDateTime) {
        log.debug("localDateTime:{}", localDateTime);

        DatesDto demoDto = new DatesDto();
        demoDto.setLocalDateTimeField(localDateTime);

        return new ResponseEntity<>(demoDto, HttpStatus.OK);
    }

    @GetMapping("/date")
    public ResponseEntity<DatesDto> date(@RequestParam("date") Date date) {
        log.debug("date:{}", date);

        DatesDto demoDto = new DatesDto();
        demoDto.setOldDateField(date);

        return new ResponseEntity<>(demoDto, HttpStatus.OK);
    }

    @GetMapping("/date-time-format-annotation_to_date")
    public ResponseEntity<DatesDto> dateTimeFormatAnnotation(
            @RequestParam("date") @DateTimeFormat(pattern = "dd.MM") Date date) {

        DatesDto demoDto = new DatesDto();
        demoDto.setOldDateField(date);

        return new ResponseEntity<>(demoDto, HttpStatus.OK);
    }

    @GetMapping("/date-time-format-annotation_to_string")
    public ResponseEntity<DatesDto> dateInParameter(
            @RequestParam("date") @DateTimeFormat(pattern = "dasdasdasdd.MM") String date)
            throws ParseException {

        DatesDto demoDto = new DatesDto();
        demoDto.setOldDateField(dateUtils.stringToDate(date));

        return new ResponseEntity<>(demoDto, HttpStatus.OK);
    }

    @PostMapping("/json-format-annotation-in-dto")
    public ResponseEntity<DatesDto> jsonFormat(@RequestBody DatesDto datesDto) {
        log.debug("dto:{}", datesDto);

        return new ResponseEntity<>(datesDto, HttpStatus.OK);
    }

    @PostMapping("/json-format-annotation-in-dto-for-localdate")
    public ResponseEntity<DatesDto> jsonFormatForLocalDate(@RequestBody DatesDto datesDto) {
        log.debug("dto:{}", datesDto);

        return new ResponseEntity<>(datesDto, HttpStatus.OK);
    }

    @GetMapping("/multiples-parameters-in-map")
    public ResponseEntity<DatesDto> multiplesParams(@RequestParam Map<String, String> allParams)
            throws ParseException {
        log.debug("localDateTime:{}", allParams.get("date"));

        DatesDto demoDto = new DatesDto();
        demoDto.setOldDateField(dateUtils.stringToDate(allParams.get("date")));

        return new ResponseEntity<>(demoDto, HttpStatus.OK);
    }

    @PostMapping(value = "/from-spa")
    public ResponseEntity<DatesDto> getDatesInJsonWithString(@RequestBody DatesDto datesDto) {
        log.debug("dto:{}", datesDto);

        return new ResponseEntity<>(datesDto, HttpStatus.OK);
    }
}
