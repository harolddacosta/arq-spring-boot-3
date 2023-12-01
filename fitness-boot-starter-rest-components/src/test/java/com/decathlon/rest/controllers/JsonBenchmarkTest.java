/* Decathlon (C)2023 */
package com.decathlon.rest.controllers;

import static org.awaitility.Awaitility.*;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.decathlon.rest.config.DatesConfiguration;
import com.decathlon.rest.config.JsonConfiguration;
import com.decathlon.rest.context.properties.RestConfigParameters;
import com.decathlon.rest.utils.DateUtils;

import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@WebMvcTest(controllers = {DateRestController.class})
@Import({
    RestConfigParameters.class,
    JsonConfiguration.class,
    DatesConfiguration.class,
    DateUtils.class
})
@TestPropertySource(locations = {"classpath:rest.properties", "classpath:application.properties"})
@Slf4j
class JsonBenchmarkTest {

    @Autowired private MockMvc mockMvc;

    ExecutorService executor = Executors.newFixedThreadPool(10);

    void concurrentWork() {
        executor.submit(
                () -> {
                    try {
                        mockMvc.perform(get("/api/v1/dates-handler"))
                                .andDo(print())
                                .andExpect(
                                        jsonPath(
                                                "$.local_date_field",
                                                is("21/10/2020"))) // Without conversion 2020-10-21
                                .andExpect(
                                        jsonPath(
                                                "$.local_date_time_field",
                                                is("2020-10-21T10:00:50.000Z")))
                                .andExpect(
                                        jsonPath(
                                                "$.old_date_field", is("2020-11-21T10:00:50.000Z")))
                                .andExpect(jsonPath("$.name").doesNotHaveJsonPath())
                                .andExpect(
                                        header().string(
                                                        "Content-Type",
                                                        "application/json;charset=UTF-8"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    @Test
    void do_benchmark_time_test() {
        AtomicInteger i = new AtomicInteger(0);

        for (; i.incrementAndGet() <= 100000; ) {
            concurrentWork();
        }

        await().until(() -> i.get() >= 100000);
    }
}
