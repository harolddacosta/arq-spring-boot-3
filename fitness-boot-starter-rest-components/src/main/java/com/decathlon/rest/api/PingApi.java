/* Decathlon (C)2022 */
package com.decathlon.rest.api;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@RequestMapping("/v1/ping")
public interface PingApi {

    @GetMapping
    public void ping(HttpServletResponse response) throws IOException;
}
