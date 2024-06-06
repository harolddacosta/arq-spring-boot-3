/* AssentSoftware (C)2023 */
package com.decathlon.rest.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;

@Slf4j
public class ExceptionUtils {

    private ExceptionUtils() {
        // Utility class
    }

    public static <T> Optional<T> convertFromHttpClientErrorException(
            HttpClientErrorException httpClientErrorException,
            ObjectMapper converter,
            Class<T> clazz) {
        try {
            return Optional.ofNullable(
                    converter.readValue(httpClientErrorException.getResponseBodyAsString(), clazz));
        } catch (Exception e) {
            log.error("Error response created by the framework");

            return Optional.empty();
        }
    }

    public static <T> Optional<T> convertFromHttpClientErrorException(
            byte[] body, ObjectMapper converter, Class<T> clazz) {
        try {
            return Optional.ofNullable(converter.readValue(body, clazz));
        } catch (Exception e) {
            log.error("Error response created by the framework");

            return Optional.empty();
        }
    }
}
