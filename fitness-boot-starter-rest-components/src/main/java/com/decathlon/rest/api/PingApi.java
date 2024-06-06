/* AssentSoftware (C)2022 */
package com.decathlon.rest.api;

import org.springframework.boot.actuate.health.HealthComponent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/v1/ping")
public interface PingApi {

    @GetMapping
    HealthComponent ping();
}
