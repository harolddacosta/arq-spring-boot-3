/* Decathlon (C)2023 */
package com.decathlon.data.tests;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.decathlon.data.JPAIntegrationApplication;
import com.decathlon.data.dto.PersonDto;
import com.decathlon.data.utils.ObjectsBuilderUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Locale;
import java.util.UUID;

@SpringBootTest(classes = JPAIntegrationApplication.class)
@AutoConfigureMockMvc
class JPAExceptionMappingTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private ObjectMapper objectMapper;

    private Faker faker = new Faker(new Locale("es", "ES"));

    @Test
    void error_when_identity_document_exists_on_second_save() throws Exception {
        PersonDto entityToSave = ObjectsBuilderUtils.createFullPersonDto(faker);
        entityToSave.setIdentityDocument(UUID.randomUUID().toString().substring(0, 16));

        mockMvc.perform(
                        post("/api/v1/persons")
                                .content(objectMapper.writeValueAsString(entityToSave))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(status().isCreated());

        mockMvc.perform(
                        post("/api/v1/persons")
                                .content(objectMapper.writeValueAsString(entityToSave))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(header().string("Content-Type", "application/problem+json"))
                .andExpect(jsonPath("$.title", is("Conflict")))
                .andExpect(jsonPath("$.status", is(409)))
                .andExpect(
                        jsonPath(
                                "$.detail",
                                is(
                                        "Data constraint violation: 'El documento de identificacion ya existe'")))
                .andExpect(
                        jsonPath(
                                "$.constraint_key", is("exception.document.identification.exists")))
                .andExpect(status().isConflict());
    }

    @Test
    void error_when_code_is_same_on_second_save() throws Exception {
        PersonDto entityToSave = ObjectsBuilderUtils.createFullPersonDto(faker);
        entityToSave.setIdentityDocument(UUID.randomUUID().toString().substring(0, 16));

        mockMvc.perform(
                        post("/api/v1/persons/code-not-autogenerated")
                                .content(objectMapper.writeValueAsString(entityToSave))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code", is("same code")))
                .andExpect(status().isCreated());

        entityToSave.setIdentityDocument(UUID.randomUUID().toString().substring(0, 16));

        mockMvc.perform(
                        post("/api/v1/persons/code-not-autogenerated")
                                .content(objectMapper.writeValueAsString(entityToSave))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(header().string("Content-Type", "application/problem+json"))
                .andExpect(jsonPath("$.title", is("Conflict")))
                .andExpect(jsonPath("$.status", is(409)))
                .andExpect(
                        jsonPath(
                                "$.detail",
                                is(
                                        "Data constraint violation: foreign key found 'ERROR: duplicate key value violates unique constraint \"uq_message_not_found_coverage\"\n  Detail: Key (codigo_persona)=(same code) already exists.' but not found the description for the constraint in the resource bundle")))
                .andExpect(
                        jsonPath(
                                "$.constraint_key",
                                is("exception.constraint.translation.undefined")))
                .andExpect(status().isConflict());
    }

    @Test
    void error_when_msas_is_same_on_second_save() throws Exception {
        PersonDto entityToSave = ObjectsBuilderUtils.createFullPersonDto(faker);
        entityToSave.setIdentityDocument(UUID.randomUUID().toString().substring(0, 16));

        mockMvc.perform(
                        post("/api/v1/persons")
                                .content(objectMapper.writeValueAsString(entityToSave))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(status().isCreated());

        entityToSave.setIdentityDocument(UUID.randomUUID().toString().substring(0, 16));

        mockMvc.perform(
                        post("/api/v1/persons")
                                .content(objectMapper.writeValueAsString(entityToSave))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(header().string("Content-Type", "application/problem+json"))
                .andExpect(jsonPath("$.title", is("Conflict")))
                .andExpect(jsonPath("$.status", is(409)))
                .andExpect(
                        jsonPath(
                                "$.detail",
                                is("Data constraint violation: 'exception.msas.exists'")))
                .andExpect(jsonPath("$.constraint_key", is("exception.msas.exists")))
                .andExpect(status().isConflict());
    }

    @Test
    void error_get_not_found_entity() throws Exception {
        mockMvc.perform(get("/api/v1/persons/{id}", 123L).characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(header().string("Content-Type", "application/problem+json"))
                .andExpect(jsonPath("$.title", is("Not Found")))
                .andExpect(status().isNotFound());
    }
}
