/* Decathlon (C)2023 */
package com.decathlon.rest.config;

import com.decathlon.rest.context.properties.RestConfigParameters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@Configuration
public class DatesConfiguration {

    @Autowired private RestConfigParameters restConfigParameters;

    @Bean
    Formatter<LocalDate> localDateFormatter() {
        return new Formatter<>() {
            private final DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern(restConfigParameters.getDates().getDateFormat());

            @Override
            public LocalDate parse(String text, Locale locale) {
                return LocalDate.parse(text, formatter);
            }

            @Override
            public String print(LocalDate object, Locale locale) {
                return formatter.format(object);
            }
        };
    }

    @Bean
    Formatter<LocalDateTime> localDateTimeFormatter() {
        return new Formatter<>() {
            private final DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern(
                            restConfigParameters.getDates().getDateTimeFormat());

            @Override
            public LocalDateTime parse(String text, Locale locale) {
                return LocalDateTime.parse(text, formatter);
            }

            @Override
            public String print(LocalDateTime object, Locale locale) {
                return formatter.format(object);
            }
        };
    }

    @Bean
    Formatter<Date> dateFormatter() {
        return new Formatter<>() {
            private final SimpleDateFormat sdf =
                    new SimpleDateFormat(restConfigParameters.getDates().getDateTimeFormat());

            @Override
            public Date parse(String text, Locale locale) throws ParseException {
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

                return sdf.parse(text);
            }

            @Override
            public String print(Date object, Locale locale) {
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

                return sdf.format(object);
            }
        };
    }
}
