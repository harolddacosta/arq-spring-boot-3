/* AssentSoftware (C)2023 */
package com.decathlon.properties;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("com.decathlon.properties")
@PropertySource("classpath:externalized.properties")
public class ExternalizedPropertiesConfiguration {}
