/* AssentSoftware (C)2023 */
package com.decathlon.data;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan("com.decathlon.data")
@PropertySource("classpath:jpa.properties")
@EnableTransactionManagement
public class JPAConfiguration {}
