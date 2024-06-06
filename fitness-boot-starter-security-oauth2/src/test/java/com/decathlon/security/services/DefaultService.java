/* AssentSoftware (C)2023 */
package com.decathlon.security.services;

import jakarta.annotation.security.RolesAllowed;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DefaultService {

    @RolesAllowed("VIEWER")
    public void checkSecuredAnnotation() {
        log.debug("Trying to execute a @Secured method");
    }

    @RolesAllowed("VIEWER")
    public void checkRolesAllowedAnnotation() {
        log.debug("Trying to execute a @RolesAllowed method");
    }

    @PreAuthorize("hasRole('ROLE_VIEWER')")
    public void checkPreauthorizeAnnotation() {
        log.debug("Trying to execute a @RolesAllowed method");
    }
}
