/* Decathlon (C)2023 */
package com.decathlon.security.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

class UserInformationEndPointConfigurationTest {

    @Test
    void userInfoRestTemplateConfiguration() {
        UserInformationEndPointConfiguration configuration =
                new UserInformationEndPointConfiguration();
        RestTemplate restTemplate = configuration.restTemplateForUserInfo();
        configuration.userInformationService("http://localhost:8080/user-info-uri", restTemplate);

        assertNotNull(restTemplate);
    }
}
