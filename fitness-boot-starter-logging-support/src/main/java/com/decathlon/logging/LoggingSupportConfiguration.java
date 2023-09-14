/* Decathlon (C)2023 */
package com.decathlon.logging;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("com.decathlon.logging")
@PropertySource("classpath:logging.properties")
public class LoggingSupportConfiguration {}
