/* Decathlon (C)2023 */
package com.decathlon.rest.controllers;

import com.decathlon.rest.context.i18n.Translator;
import com.decathlon.rest.utils.dtos.KeyValueResponseDto;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/language-test")
public class TranslationRestController {

    private final Translator translator;

    @GetMapping
    public ResponseEntity<KeyValueResponseDto> languageHeaderResponse() {
        return new ResponseEntity<>(
                new KeyValueResponseDto(translator.toLocale("msg.translated")), HttpStatus.OK);
    }
}
