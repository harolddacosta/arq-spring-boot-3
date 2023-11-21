/* Decathlon (C)2023 */
package com.decathlon.rest.controllers;

import com.decathlon.rest.utils.dtos.KeyValueResponseDto;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/multipart")
public class MultipartRestController {

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<KeyValueResponseDto> languageHeaderResponse(
            @RequestPart MultipartFile document) {
        return new ResponseEntity<>(new KeyValueResponseDto("done"), HttpStatus.OK);
    }
}
