/* AssentSoftware (C)2023 */
package com.decathlon.security.test;

import static org.assertj.core.api.Assertions.assertThat;

import com.decathlon.security.jwt.converters.AbstractRolesConverter;
import com.decathlon.security.jwt.converters.ResourceRolesConverter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(
        properties = {
            "spring.security.oauth2.resourceserver.m2m.enabled=true",
            "spring.security.oauth2.resourceserver.jwt.decoder-type=publicKey",
            "spring.security.oauth2.resourceserver.jwt.public-key-type=pem",
            "spring.security.oauth2.resourceserver.jwt.public-key-location=classpath:public-key.pem",
            "spring.security.oauth2.resourceserver.m2m.jwt.decoder-type=publicKey",
            "spring.security.oauth2.resourceserver.m2m.jwt.public-key-type=pem",
            "spring.security.oauth2.resourceserver.m2m.jwt.public-key-location=classpath:public-key.pem"
        })
class M2mPublicKeyDecodersTest {

    @Autowired JwtDecoder regularDecoder;
    @Autowired JwtDecoder m2mDecoder;

    @TestConfiguration
    public static class M2mDecodersTestConfiguration {
        @Bean
        AbstractRolesConverter rolesResolver(
                @Value("${spring.security.oauth2.resourceserver.jwt.resource-access-name}")
                        String resourceAccessName) {
            return new ResourceRolesConverter(resourceAccessName);
        }
    }

    @Test
    void contextLoads(ApplicationContext context) {
        assertThat(context).isNotNull();
        assertThat(regularDecoder).isNotNull();
        assertThat(m2mDecoder).isNotNull();

        NimbusJwtDecoder nimbusJwtDecoder = (NimbusJwtDecoder) regularDecoder;
        Jwt jwt =
                nimbusJwtDecoder.decode(
                        "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0.NHVaYe26MbtOYhSKkoKYdFVomg4i8ZJd8_-RU8VNbftc4TSMb4bXP3l3YlNWACwyXPGffz5aXHc6lty1Y2t4SWRqGteragsVdZufDn5BlnJl9pdR_kdVFUsra2rWKEofkZeIC4yWytE58sMIihvo9H1ScmmVwBcQP6XETqYd0aSHp1gOa9RdUPDvoXQ5oqygTqVtxaDr6wUFKrKItgBMzWIdNZ6y7O9E0DhEPTbE9rfBo6KTFsHAZnMg4k68CDp2woYIaXbmYTWcvbzIuHO7_37GT79XdIwkm95QJ7hYC9RiwrV7mesbY4PAahERJawntho0my942XheVLmGwLMBkQ");

        NimbusJwtDecoder m2mJwtDecoder = (NimbusJwtDecoder) m2mDecoder;
        Jwt jwtForM2mDecoder =
                m2mJwtDecoder.decode(
                        "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0.NHVaYe26MbtOYhSKkoKYdFVomg4i8ZJd8_-RU8VNbftc4TSMb4bXP3l3YlNWACwyXPGffz5aXHc6lty1Y2t4SWRqGteragsVdZufDn5BlnJl9pdR_kdVFUsra2rWKEofkZeIC4yWytE58sMIihvo9H1ScmmVwBcQP6XETqYd0aSHp1gOa9RdUPDvoXQ5oqygTqVtxaDr6wUFKrKItgBMzWIdNZ6y7O9E0DhEPTbE9rfBo6KTFsHAZnMg4k68CDp2woYIaXbmYTWcvbzIuHO7_37GT79XdIwkm95QJ7hYC9RiwrV7mesbY4PAahERJawntho0my942XheVLmGwLMBkQ");

        assertThat(jwt.getClaimAsString("name")).isEqualTo("John Doe");
        assertThat(jwtForM2mDecoder.getClaimAsString("name")).isEqualTo("John Doe");
    }
}
