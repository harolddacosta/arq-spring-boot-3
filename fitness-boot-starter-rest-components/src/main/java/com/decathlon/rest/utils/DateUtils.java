/* Decathlon (C)2023 */
package com.decathlon.rest.utils;

import lombok.RequiredArgsConstructor;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class DateUtils {

    private final Formatter<LocalDate> localDateFormatter;
    private final Formatter<LocalDateTime> localDateTimeFormatter;
    private final Formatter<Date> dateFormatter;

    public String dateToString(Date date) {
        return dateFormatter.print(date, Locale.getDefault());
    }

    public Date stringToDate(String date) throws ParseException {
        return dateFormatter.parse(date, Locale.getDefault());
    }

    public String localDateToString(LocalDate date) {
        return localDateFormatter.print(date, Locale.getDefault());
    }

    public LocalDate stringToLocalDate(String date) throws ParseException {
        return localDateFormatter.parse(date, Locale.getDefault());
    }

    public String localDateTimeToString(LocalDateTime date) {
        return localDateTimeFormatter.print(date, Locale.getDefault());
    }

    public LocalDateTime stringToLocalDateTime(String date) throws ParseException {
        return localDateTimeFormatter.parse(date, Locale.getDefault());
    }
}
