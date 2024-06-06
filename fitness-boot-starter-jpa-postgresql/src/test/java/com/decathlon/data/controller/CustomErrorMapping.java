/* AssentSoftware (C)2023 */
package com.decathlon.data.controller;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.zalando.problem.spring.web.advice.ProblemHandling;

@RestControllerAdvice
public class CustomErrorMapping implements ProblemHandling {}
