/* AssentSoftware (C)2023 */
package com.decathlon.data.converters;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class StringListConverterTest {

    @Test
    void when_convert_to_string_ok() {
        StringListConverter converter = new StringListConverter();

        String stringList = converter.convertToDatabaseColumn(List.of("Priueba1", "Priueba2"));

        Assertions.assertThat(stringList).isEqualTo("Priueba1|Priueba2");
    }

    @Test
    void when_convert_to_string_error() {
        StringListConverter converter = new StringListConverter();

        String stringList = converter.convertToDatabaseColumn(List.of("Priueba1|", "Priueba2"));

        Assertions.assertThat(stringList).isEqualTo("Priueba1||Priueba2");
    }

    @Test
    void when_convert_to_list_ok() {
        StringListConverter converter = new StringListConverter();

        List<String> stringList = converter.convertToEntityAttribute("Priueba1|Priueba2");

        Assertions.assertThat(stringList).contains("Priueba1", "Priueba2");
    }

    @Test
    void when_convert_to_list_error() {
        StringListConverter converter = new StringListConverter();

        List<String> stringList = converter.convertToEntityAttribute("Priueba1||Priueba2");

        Assertions.assertThat(stringList).contains("Priueba1", "Priueba2");
    }

    @Test
    void when_convert_to_null() {
        StringListConverter converter = new StringListConverter();

        String stringList = converter.convertToDatabaseColumn(null);

        Assertions.assertThat(stringList).isNullOrEmpty();
    }

    @Test
    void when_convert_to_empty() {
        StringListConverter converter = new StringListConverter();

        String stringList = converter.convertToDatabaseColumn(List.of());

        Assertions.assertThat(stringList).isNullOrEmpty();
    }

    @Test
    void when_convert_to_list_null() {
        StringListConverter converter = new StringListConverter();

        List<String> stringList = converter.convertToEntityAttribute(null);

        Assertions.assertThat(stringList).isNullOrEmpty();
    }
}
