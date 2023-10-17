/* Decathlon (C)2022 */
package com.decathlon.rest.controllers;

import com.decathlon.rest.api.PingApi;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.actuate.health.HealthComponent;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PingController implements PingApi {

    private final HealthEndpoint healthEndpoint;

    @Override
    public HealthComponent ping() {
        return healthEndpoint.health();
    }
}
