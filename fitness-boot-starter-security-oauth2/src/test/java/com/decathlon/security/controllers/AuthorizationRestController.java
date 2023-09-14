/* Decathlon (C)2023 */
package com.decathlon.security.controllers;

import com.decathlon.security.services.DefaultService;
import com.decathlon.security.utils.dtos.KeyValueResponseDto;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthorizationRestController {

    private final DefaultService defaultService;

    @GetMapping("/public/no-jwt-needed")
    public ResponseEntity<KeyValueResponseDto> noJwtNeeded() {
        return new ResponseEntity<KeyValueResponseDto>(
                new KeyValueResponseDto("Authorized"), HttpStatus.OK);
    }

    @GetMapping("/protected/by-any-rule")
    public ResponseEntity<KeyValueResponseDto> protectedByAnyRule() {
        return new ResponseEntity<KeyValueResponseDto>(
                new KeyValueResponseDto("Authorized"), HttpStatus.OK);
    }

    @GetMapping("/protected/user/info")
    public Jwt getUserInfo(@AuthenticationPrincipal Jwt principal) {
        return principal;
    }

    @GetMapping("/read-only/check")
    public ResponseEntity<KeyValueResponseDto> checkScopeBasedEndpoint() {
        return new ResponseEntity<KeyValueResponseDto>(
                new KeyValueResponseDto("Authorized"), HttpStatus.OK);
    }

    @GetMapping("/role-based/check")
    public ResponseEntity<KeyValueResponseDto> checkRoleBasedEndpoint() {
        return new ResponseEntity<KeyValueResponseDto>(
                new KeyValueResponseDto("Authorized"), HttpStatus.OK);
    }

    @GetMapping("/method-based/check_secured_annotation")
    public ResponseEntity<KeyValueResponseDto> securedMethodBasedEndpoint() {
        defaultService.checkSecuredAnnotation();

        return new ResponseEntity<KeyValueResponseDto>(
                new KeyValueResponseDto("Authorized"), HttpStatus.OK);
    }

    @GetMapping("/method-based/check_roles_allowed_annotation")
    public ResponseEntity<KeyValueResponseDto> rolesAllowedMethodBasedEndpoint() {
        defaultService.checkRolesAllowedAnnotation();

        return new ResponseEntity<KeyValueResponseDto>(
                new KeyValueResponseDto("Authorized"), HttpStatus.OK);
    }

    @GetMapping("/method-based/check_preauthorize_annotation")
    public ResponseEntity<KeyValueResponseDto> preAuthorizedMethodBasedEndpoint() {
        defaultService.checkPreauthorizeAnnotation();

        return new ResponseEntity<KeyValueResponseDto>(
                new KeyValueResponseDto("Authorized"), HttpStatus.OK);
    }
}
