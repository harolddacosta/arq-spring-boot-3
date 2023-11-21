/* Decathlon (C)2023 */
package com.decathlon.rest.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.decathlon.rest.config.DatesConfiguration;
import com.decathlon.rest.config.JsonConfiguration;
import com.decathlon.rest.context.properties.RestConfigParameters;
import com.decathlon.rest.dtos.DatesDto;
import com.decathlon.rest.utils.DateUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

// @formatter:off
/**
 * With this tests we can put in place an unique way for handling dates in all ways in the app by
 * now, we must define the common date defined in the app and choose one to define here.
 *
 * <p>The tests made here are in dtos, serializers, deserializers, and request params <br>
 * We must look up for: 1. @JsonFormat for dates, and remove then when match the global one 2. Look
 * for strings
 * 'dd/MM/yyyy','yyyy/MM/dd','dd-MM-yyyy','yyyy-MM-dd','dd.MM.yyyy','yyyy.MM.dd','ddMMyyyy','yyyyMMdd'
 * 3. Look for strings
 * 'dd/MM/yy','yy/MM/dd','dd-MM-yy','yy-MM-dd','dd.MM.yy','yy.MM.dd','ddMMyy','yyMMdd' 4. Look for
 * DateTimeFormatter, .parse, new SimpleDateFormat( 5. Look for @JsonSerialize and @JsonDeserialize
 * and don't use them,CustomDateDeserializer,CustomDateSerializer Facts: 1. The @JsonFormat defined
 * in Dto's replaces the global one defined 2. The CustomDateUtils defined in patients must be
 * redefined cause of hard coded date formats 3. The definition of a Formatter<Date>, disable all
 * the @DateTimeFormat defined in mapping controllers 4. The @DateTimeFormat(pattern = "yyyy-MM-dd")
 * with String has no effect over the transformation 5. Replace all the SimpleDateFormat for the
 * conversion to DateTimeFormatter by thread safe issue Do tests with: 1.
 * spring.mvc.date-format=dd/MM/yyyy has no effect anymore
 *
 * <p>The way to look up for date formats in the code with eclipse, may be using this regexp
 * ([d]{1,2}[-|.|\/]{0,1}[M|m]{1,2}[-|.|\/]{0,1}[y]{1,4})|([y]{1,4}[-|.|\/]{0,1}[M|m]{1,2}[-|.|\/]{0,1}[d]{1,2})|([M|m]{1,2}[-|.|\/]{0,1}[d]{1,2}[-|.|\/]{0,1}[y]{1,4})
 *
 * <p>With this regexp, you can find formats in the code to try to externalize using CustomDateUtils
 *
 * @author hdacosta
 */

// @formatter:on
@SuppressWarnings("deprecation")
@WebMvcTest(controllers = {DateRestController.class})
@Import({
    RestConfigParameters.class,
    JsonConfiguration.class,
    DatesConfiguration.class,
    DateUtils.class
})
@TestPropertySource(locations = {"classpath:application.properties", "classpath:rest.properties"})
class DateRestControllerTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private ObjectMapper objectMapper;

    @Autowired private DateUtils dateUtils;

    @Test
    void when_no_formatter_date_on_get() throws Exception {
        mockMvc.perform(get("/api/v1/dates-handler"))
                .andDo(print())
                .andExpect(
                        jsonPath(
                                "$.local_date_field",
                                is("21/10/2020"))) // Without conversion 2020-10-21
                .andExpect(jsonPath("$.local_date_time_field", is("2020-10-21T10:00:50.000Z")))
                .andExpect(jsonPath("$.old_date_field", is("2020-11-21T10:00:50.000Z")))
                .andExpect(jsonPath("$.name").doesNotHaveJsonPath())
                .andExpect(header().string("Content-Type", "application/json;charset=UTF-8"));

        mockMvc.perform(get("/api/v1/dates-handler").param("add_nullable_value", "true"))
                .andDo(print())
                .andExpect(jsonPath("$.local_date_field", is("21/10/2020")))
                .andExpect(jsonPath("$.local_date_time_field", is("2020-10-21T10:00:50.000Z")))
                .andExpect(jsonPath("$.old_date_field", is("2020-11-21T10:00:50.000Z")))
                .andExpect(jsonPath("$.name", is("Harold")))
                .andExpect(header().string("Content-Type", "application/json;charset=UTF-8"));
    }

    @Test
    void when_no_formatter_date_on_post() throws Exception {
        DatesDto demoDto = new DatesDto();
        demoDto.setLocalDateField(dateUtils.stringToLocalDate("21/10/2020"));
        demoDto.setLocalDateTimeField(dateUtils.stringToLocalDateTime("2020-11-21T10:00:50.000Z"));
        demoDto.setOldDateField(dateUtils.stringToDate("2020-11-21T09:00:50.000Z"));

        mockMvc.perform(
                        post("/api/v1/dates-handler")
                                .content(objectMapper.writeValueAsString(demoDto))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(
                        jsonPath(
                                "$.local_date_field",
                                is(dateUtils.localDateToString(demoDto.getLocalDateField()))))
                .andExpect(
                        jsonPath(
                                "$.local_date_time_field",
                                is(
                                        dateUtils.localDateTimeToString(
                                                demoDto.getLocalDateTimeField()))))
                .andExpect(jsonPath("$.old_date_field", is("2020-11-21T09:00:50.000Z")))
                .andExpect(jsonPath("$.name").doesNotHaveJsonPath())
                .andExpect(header().string("Content-Type", "application/json;charset=UTF-8"));
    }

    @Test
    void when_localdate_in_parameter() throws Exception {
        DatesDto demoDto = new DatesDto();
        demoDto.setLocalDateField(LocalDate.of(2020, 10, 21));

        mockMvc.perform(
                        get("/api/v1/dates-handler/localdate")
                                .param(
                                        "date",
                                        dateUtils.localDateToString(demoDto.getLocalDateField())))
                .andDo(print())
                .andExpect(jsonPath("$.local_date_field", is("21/10/2020"))) // Without conversion
                // 2020-10-21
                .andExpect(header().string("Content-Type", "application/json;charset=UTF-8"));
    }

    @Test
    void when_localdatetime_in_parameter() throws Exception {
        DatesDto demoDto = new DatesDto();
        demoDto.setLocalDateTimeField(LocalDateTime.of(2020, 10, 21, 10, 00, 50));

        mockMvc.perform(
                        get("/api/v1/dates-handler/localdatetime")
                                .param(
                                        "date",
                                        dateUtils.localDateTimeToString(
                                                demoDto.getLocalDateTimeField())))
                .andDo(print())
                .andExpect(jsonPath("$.local_date_time_field", is("2020-10-21T10:00:50.000Z")))
                // Without conversion 2020-10-21T10:00:50
                .andExpect(header().string("Content-Type", "application/json;charset=UTF-8"));
    }

    @Test
    void when_old_date_in_parameter() throws Exception {
        DatesDto demoDto = new DatesDto();
        demoDto.setOldDateField(new Date(120, 10, 21, 10, 00, 50));

        mockMvc.perform(
                        get("/api/v1/dates-handler/date")
                                .param("date", dateUtils.dateToString(demoDto.getOldDateField())))
                .andDo(print())
                .andExpect(jsonPath("$.old_date_field", is("2020-11-21T10:00:50.000Z")))
                // Without conversion 2020-11-21T09:00:50.000+0000
                .andExpect(header().string("Content-Type", "application/json;charset=UTF-8"));
    }

    @ParameterizedTest
    @ValueSource(
            strings = {
                "/api/v1/dates-handler/date-time-format-annotation_to_date",
                "/api/v1/dates-handler/date-time-format-annotation_to_string"
            })
    void when_date_time_format_annotation_not_working(String url) throws Exception {
        DatesDto demoDto = new DatesDto();
        demoDto.setOldDateField(new Date(120, 10, 21, 10, 00, 50));

        mockMvc.perform(
                        get(url).param("date", dateUtils.dateToString(demoDto.getOldDateField()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(jsonPath("$.old_date_field", is("2020-11-21T10:00:50.000Z")))
                // Without conversion 2020-11-21T09:00:50.000+0000
                .andExpect(header().string("Content-Type", "application/json;charset=UTF-8"));
    }

    @Test
    void when_date_with_different_jsonformat_in_body() throws Exception {
        DatesDto demoDto = new DatesDto();
        demoDto.setJsonFormatDate(new Date(120, 10, 21, 10, 00, 50));

        mockMvc.perform(
                        post("/api/v1/dates-handler/json-format-annotation-in-dto")
                                .content(objectMapper.writeValueAsString(demoDto))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(jsonPath("$.json_format_date", is("21.11.2020")))
                .andExpect(header().string("Content-Type", "application/json;charset=UTF-8"));
    }

    @Test
    void when_date_with_different_jsonformat_for_localdate_in_body() throws Exception {
        DatesDto demoDto = new DatesDto();
        demoDto.setJsonFormatLocalDate(LocalDate.of(2020, 10, 21));

        mockMvc.perform(
                        post("/api/v1/dates-handler/json-format-annotation-in-dto-for-localdate")
                                .content(objectMapper.writeValueAsString(demoDto))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(jsonPath("$.json_format_local_date", is("21.10.2020")))
                .andExpect(header().string("Content-Type", "application/json;charset=UTF-8"));
    }

    @Test
    void when_string_date_is_received_as_map() throws Exception {
        DatesDto demoDto = new DatesDto();
        demoDto.setOldDateField(new Date(120, 10, 21, 10, 00, 50));

        mockMvc.perform(
                        get("/api/v1/dates-handler/multiples-parameters-in-map")
                                .param("date", dateUtils.dateToString(demoDto.getOldDateField()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(jsonPath("$.old_date_field", is("2020-11-21T10:00:50.000Z")))
                // Without conversion 2020-11-21T09:00:50.000+0000
                .andExpect(header().string("Content-Type", "application/json;charset=UTF-8"));
    }

    @Test
    void when_coming_from_spa_app() throws Exception {
        String jsonRequest = "{\"local_date_time_field\":\"2020-11-27T23:00:00.000Z\"}";

        mockMvc.perform(
                        post("/api/v1/dates-handler/from-spa")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(jsonPath("$.local_date_time_field", is("2020-11-27T23:00:00.000Z")));

        jsonRequest = "{\"local_date_field\":\"21/10/1981\"}";

        mockMvc.perform(
                        post("/api/v1/dates-handler/from-spa")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(jsonPath("$.local_date_field", is("21/10/1981")));
    }
}
