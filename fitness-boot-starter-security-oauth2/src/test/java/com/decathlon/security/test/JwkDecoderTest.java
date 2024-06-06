/* AssentSoftware (C)2023 */
package com.decathlon.security.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.HttpStatusCode;
import org.mockserver.springtest.MockServerTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootTest
@TestPropertySource(properties = {"spring.security.oauth2.resourceserver.jwt.decoder-type=jwk"})
@MockServerTest({
    "spring.security.oauth2.resourceserver.jwt.jwk-set-uri=http://localhost:${mockServerPort}/auth/realms/master/protocol/openid-connect/certs"
})
class JwkDecoderTest {

    @Autowired JwtDecoder jwtDecoder;

    private MockServerClient mockServerClient;

    @Test
    void contextLoads(ApplicationContext context) throws IOException {
        final var resource = new ClassPathResource("responses/keycloak_certs.json");
        final var mockedResponse = Files.readString(Path.of(resource.getURI()));

        mockServerClient
                .when(request().withPath("/auth/realms/master/protocol/openid-connect/certs"))
                .respond(
                        response()
                                .withStatusCode(HttpStatusCode.OK_200.code())
                                .withContentType(org.mockserver.model.MediaType.APPLICATION_JSON)
                                .withBody(mockedResponse));

        NimbusJwtDecoder nimbusJwtDecoder = (NimbusJwtDecoder) jwtDecoder;
        //        Jwt jwt =
        //                nimbusJwtDecoder.decode(
        //
        // "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJLNjd1MllveDl3TEhpbmx3alFxQWtHbXVkS0JzdmRmc1JmWGp2YjZjcnhvIn0.eyJleHAiOjE3MDgzOTM1NDEsImlhdCI6MTcwODM1NzU0MSwianRpIjoiOTljMzY0ODctN2ZlNC00NTAwLWEwMzQtOTdmZjgxNTUxN2JlIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo5MDkwL3JlYWxtcy9tYXN0ZXIiLCJhdWQiOlsibWFzdGVyLXJlYWxtIiwidG9fdGVzdF9kaWZmX3NpZ25hdHVyZS1yZWFsbSIsImFjY291bnQiXSwic3ViIjoiNGExZTkzMzItOTZiYS00OWFkLWFmZDEtZTFiMjBkNTlmZmM1IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiaGRhLWNsaWVudCIsInNlc3Npb25fc3RhdGUiOiI4Njk0NDMzOC01ZTU2LTRiNDEtYTliNi1kN2I4YjNhMzQyODUiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbIi8qIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJjcmVhdGUtcmVhbG0iLCJkZWZhdWx0LXJvbGVzLW1hc3RlciIsInN1cGVyX2FkbWluIiwib2ZmbGluZV9hY2Nlc3MiLCJhZG1pbiIsImhkYS11aSIsInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsibWFzdGVyLXJlYWxtIjp7InJvbGVzIjpbInZpZXctaWRlbnRpdHktcHJvdmlkZXJzIiwidmlldy1yZWFsbSIsIm1hbmFnZS1pZGVudGl0eS1wcm92aWRlcnMiLCJpbXBlcnNvbmF0aW9uIiwiY3JlYXRlLWNsaWVudCIsIm1hbmFnZS11c2VycyIsInF1ZXJ5LXJlYWxtcyIsInZpZXctYXV0aG9yaXphdGlvbiIsInF1ZXJ5LWNsaWVudHMiLCJxdWVyeS11c2VycyIsIm1hbmFnZS1ldmVudHMiLCJtYW5hZ2UtcmVhbG0iLCJ2aWV3LWV2ZW50cyIsInZpZXctdXNlcnMiLCJ2aWV3LWNsaWVudHMiLCJtYW5hZ2UtYXV0aG9yaXphdGlvbiIsIm1hbmFnZS1jbGllbnRzIiwicXVlcnktZ3JvdXBzIl19LCJ0b190ZXN0X2RpZmZfc2lnbmF0dXJlLXJlYWxtIjp7InJvbGVzIjpbInZpZXctaWRlbnRpdHktcHJvdmlkZXJzIiwidmlldy1yZWFsbSIsIm1hbmFnZS1pZGVudGl0eS1wcm92aWRlcnMiLCJpbXBlcnNvbmF0aW9uIiwiY3JlYXRlLWNsaWVudCIsIm1hbmFnZS11c2VycyIsInF1ZXJ5LXJlYWxtcyIsInZpZXctYXV0aG9yaXphdGlvbiIsInF1ZXJ5LWNsaWVudHMiLCJxdWVyeS11c2VycyIsIm1hbmFnZS1ldmVudHMiLCJtYW5hZ2UtcmVhbG0iLCJ2aWV3LWV2ZW50cyIsInZpZXctdXNlcnMiLCJ2aWV3LWNsaWVudHMiLCJtYW5hZ2UtYXV0aG9yaXphdGlvbiIsIm1hbmFnZS1jbGllbnRzIiwicXVlcnktZ3JvdXBzIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50Iiwic3VwZXJfYWRtaW4iLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoicHJvZmlsZSBlbWFpbCBvcGVuaWQiLCJzaWQiOiI4Njk0NDMzOC01ZTU2LTRiNDEtYTliNi1kN2I4YjNhMzQyODUiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsInByZWZlcnJlZF91c2VybmFtZSI6ImhkYS11aS11c2VyIiwiZ2l2ZW5fbmFtZSI6IiIsImZhbWlseV9uYW1lIjoiIn0.YiYziv_hIseD3HjGF50MyIF7gUgtaHR9adltD3k4bY0htziQEjtd3VlosrLhHzTH4oxdoAPJHJo2-tak3M3yRCMP6_0ouqFMrHyNSM4DXaJ5XH847FPOYzLXd-nsdNgIUT6kEI-0P3Toy4u_rBAd23StucqLg-SkVdP4CkYtNGRrcPUsOVUkNeOR2DBcuY_riqPUc9WPh9M82KZRYl1Al58c76efU28ztMEwRkzfvhaYL8dIkmKSuvXfZKXwpD0aK0qWkBxuvdR0EobW8PUcTkXJImo6lCBcum-xjcD4NZY5unmYM8sow8lELGNpa97BRnvCBRgSsnvbr4v8xV7RSg");

        //        assertThat(jwt.getClaimAsString("preferred_username")).isEqualTo("hda-ui-user");
        assertThat(context).isNotNull();
        assertThat(jwtDecoder).isNotNull();
    }
}
