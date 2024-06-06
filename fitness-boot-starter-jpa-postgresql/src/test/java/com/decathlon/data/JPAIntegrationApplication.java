/* AssentSoftware (C)2023 */
package com.decathlon.data;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {"com.decathlon.data.domain"})
public class JPAIntegrationApplication {}
