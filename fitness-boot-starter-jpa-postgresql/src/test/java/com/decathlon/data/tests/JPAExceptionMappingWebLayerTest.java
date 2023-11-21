/* Decathlon (C)2023 */
package com.decathlon.data.tests;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.decathlon.data.context.properties.JPAConfigParameters;
import com.decathlon.data.controller.SimpleController;
import com.decathlon.data.dto.PersonDto;
import com.decathlon.data.manager.PersonService;
import com.decathlon.data.mappers.PersonMapperImpl_;
import com.decathlon.data.utils.DefaultConstraintNameResolver;
import com.decathlon.data.utils.ObjectsBuilderUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.CannotCreateTransactionException;

import java.util.Locale;
import java.util.UUID;

@WebMvcTest(controllers = {SimpleController.class})
@Import({PersonMapperImpl_.class, DefaultConstraintNameResolver.class, JPAConfigParameters.class})
class JPAExceptionMappingWebLayerTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private ObjectMapper objectMapper;

    @MockBean private PersonService personService;

    private final Faker faker = new Faker(new Locale("es", "ES"));

    @Test
    void error_when_service_is_not_available() throws Exception {
        PersonDto entityToSave = ObjectsBuilderUtils.createFullPersonDto(faker);
        entityToSave.setIdentityDocument(UUID.randomUUID().toString().substring(0, 16));

        doThrow(new CannotCreateTransactionException(null)).when(personService).save(any());

        mockMvc.perform(
                        post("/api/v1/persons")
                                .content(objectMapper.writeValueAsString(entityToSave))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(header().string("Content-Type", "application/problem+json"))
                .andExpect(jsonPath("$.title", is("Internal Server Error")))
                .andExpect(status().isInternalServerError());
    }
}
