/* AssentSoftware (C)2023 */
package com.decathlon.rest;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("com.decathlon.rest")
@PropertySource("classpath:rest.properties")
@EnableConfigurationProperties
public class RestServicesConfiguration {}
