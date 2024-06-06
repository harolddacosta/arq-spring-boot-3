/* AssentSoftware (C)2023 */
package com.decathlon.rest.utils;

import lombok.extern.slf4j.Slf4j;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
public class MultipartUtils {

    private MultipartUtils() {
        // Utility class
    }

    public static void addMultipartToBody(
            byte[] file, MultipartBodyBuilder body, String partName, String fileName) {
        if (file != null) {
            ByteArrayResource resource =
                    new ByteArrayResource(file) {

                        @Override
                        public String getFilename() {
                            return fileName;
                        }
                    };

            body.part(partName, resource);
        }
    }

    public static void addMultipartToBody(
            MultipartFile multipartFile, MultipartBodyBuilder body, String partName) {
        try {
            if (multipartFile != null) {
                addMultipartToBody(
                        multipartFile.getBytes(),
                        body,
                        partName,
                        multipartFile.getOriginalFilename());
            }
        } catch (IOException e) {
            log.error("Problem adding multipart to body", e);
        }
    }
}
