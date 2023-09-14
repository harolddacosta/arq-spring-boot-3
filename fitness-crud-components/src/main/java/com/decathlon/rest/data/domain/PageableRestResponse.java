/* Decathlon (C)2023 */
package com.decathlon.rest.data.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class PageableRestResponse<T> extends PageImpl<T> {

    private static final long serialVersionUID = -4375777488297372358L;

    @JsonCreator(mode = Mode.PROPERTIES)
    public PageableRestResponse(
            @JsonProperty("content") List<T> content,
            @JsonProperty("number") int number,
            @JsonProperty("size") int size,
            @JsonProperty("totalElements") Long totalElements,
            @JsonProperty("pageable") JsonNode pageable,
            @JsonProperty("last") boolean last,
            @JsonProperty("totalPages") int totalPages,
            @JsonProperty("sort") JsonNode sort,
            @JsonProperty("first") boolean first,
            @JsonProperty("numberOfElements") int numberOfElements) {
        super(content, PageRequest.of(number, size), totalElements);
    }

    public PageableRestResponse(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }
}
