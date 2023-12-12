/* Decathlon (C)2023 */
package com.decathlon.support.cloud;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("com.decathlon.support.cloud")
@PropertySource("classpath:cloud.properties")
public class CloudConfiguration {
    // Added comment in release, this code should be replicated in master and develop

}
