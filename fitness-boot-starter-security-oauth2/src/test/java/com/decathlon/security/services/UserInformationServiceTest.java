/* AssentSoftware (C)2023 */
package com.decathlon.security.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import com.decathlon.security.SecurityServicesApplication;
import com.decathlon.security.jwt.model.UserInformationDetails;
import com.decathlon.security.jwt.service.UserInformationService;
import com.decathlon.security.test.utils.JwtBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = {SecurityServicesApplication.class})
class UserInformationServiceTest {

    private MockRestServiceServer mockServer;
    private final ObjectMapper mapper = new ObjectMapper();

    @InjectMocks private UserInformationService userInformationService;

    @BeforeEach
    void init() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
        restTemplate.setMessageConverters(getJsonMessageConverters());

        mockServer = MockRestServiceServer.createServer(restTemplate);

        userInformationService =
                new UserInformationService("http://localhost:8080/user-info", restTemplate);
    }

    @Test
    void when_is_public_access() throws Exception {
        UserInformationDetails userInformation = new UserInformationDetails();
        userInformation.setFamilyName("Da Costa");

        mockServer
                .expect(ExpectedCount.once(), requestTo(new URI("http://localhost:8080/user-info")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(mapper.writeValueAsString(userInformation)));

        String token =
                JwtBuilder.getInstance()
                        .secretKey("QzPuxfiQlsZyddSNQPjL8cr3mod4D89j")
                        .scope(new String[] {"profile email openid"})
                        .build();

        userInformation = userInformationService.getUserInformation("rachel", token);

        assertThat(userInformation.getFamilyName()).isEqualTo("Da Costa");
    }

    @Test
    void when_is_public_access_error() throws Exception {
        UserInformationDetails userInformation = new UserInformationDetails();
        userInformation.setFamilyName("Da Costa");

        mockServer
                .expect(ExpectedCount.once(), requestTo(new URI("http://localhost:8080/user-info")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withStatus(HttpStatus.UNAUTHORIZED)
                                .contentType(MediaType.APPLICATION_JSON));

        String token =
                JwtBuilder.getInstance()
                        .secretKey("QzPuxfiQlsZyddSNQPjL8cr3mod4D89j")
                        .scope(new String[] {"profile email openid"})
                        .build();

        assertThatThrownBy(
                        () -> {
                            userInformationService.getUserInformation("rachel", token);
                        })
                .isInstanceOf(AccessDeniedException.class)
                .hasMessageContaining(
                        "Calling user-info endpoint with unauthorized/expired credentials");
    }

    private List<HttpMessageConverter<?>> getJsonMessageConverters() {
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        converters.add(new MappingJackson2HttpMessageConverter());
        converters.add(new FormHttpMessageConverter());

        return converters;
    }
}
