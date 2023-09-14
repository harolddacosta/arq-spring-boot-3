/* Decathlon (C)2022 */
package com.decathlon.rest.controllers;

import com.decathlon.rest.api.PingApi;

import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class PingController implements PingApi {

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Override
    public void ping(HttpServletResponse response) throws IOException {
        response.sendRedirect(contextPath + "/actuator/health");
    }
}
