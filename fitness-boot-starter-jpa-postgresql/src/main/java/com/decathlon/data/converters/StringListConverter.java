/* Decathlon (C)2023 */
package com.decathlon.data.converters;

import jakarta.persistence.AttributeConverter;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringListConverter implements AttributeConverter<List<String>, String> {

    public String convertToDatabaseColumn(List<String> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }

        return String.join("|", list);
    }

    public List<String> convertToEntityAttribute(String joined) {
        // I wrap Arrays.asList in an ArrayList in the converter, because the result is
        // stored in
        // the attribute and any change to that list will be written back to the
        // database
        if (joined == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(Arrays.asList(StringUtils.split(joined, "|")));
    }
}
