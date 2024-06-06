/* AssentSoftware (C)2023 */
package com.decathlon.rest.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.decathlon.rest.config.JsonConfiguration;
import com.decathlon.rest.config.LocaleConfiguration;
import com.decathlon.rest.context.i18n.Translator;
import com.decathlon.rest.context.properties.RestConfigParameters;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

@WebMvcTest(controllers = {MultipartRestController.class})
@Import({
    RestConfigParameters.class,
    Translator.class,
    LocaleConfiguration.class,
    JsonConfiguration.class
})
@TestPropertySource(locations = {"classpath:rest.properties", "classpath:application.properties"})
class MultipartRestControllerTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private ResourceLoader resourceLoader = null;

    @Test
    void ok_when_file_is_size_allowed() throws Exception {
        MockMultipartFile document =
                new MockMultipartFile("document", getTestFile("5MBFile.txt").getInputStream());

        mockMvc.perform(multipart("/api/v1/multipart").file(document))
                .andDo(print())
                .andExpect(status().isOk());
    }

    private FileSystemResource getTestFile(String name) throws IOException {
        return new FileSystemResource(
                resourceLoader.getResource("classpath:filesToUpload/" + name).getFile());
    }
}
