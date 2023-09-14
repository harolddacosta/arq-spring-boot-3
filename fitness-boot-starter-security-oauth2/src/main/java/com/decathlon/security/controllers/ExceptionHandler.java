/* Decathlon (C)2023 */
package com.decathlon.security.controllers;

import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.zalando.problem.spring.web.advice.security.SecurityAdviceTrait;

@ControllerAdvice
@Order(1)
public class ExceptionHandler implements SecurityAdviceTrait {}
