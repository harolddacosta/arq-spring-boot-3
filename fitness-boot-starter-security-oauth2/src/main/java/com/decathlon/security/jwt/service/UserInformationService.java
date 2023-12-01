/* Decathlon (C)2023 */
package com.decathlon.security.jwt.service;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

import com.decathlon.security.jwt.model.UserInformationDetails;

import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
public final class UserInformationService {

    private final String userInfoUri;
    private final RestTemplate restTemplate;

    @Cacheable(key = "#sub", cacheNames = "userInformation")
    public UserInformationDetails getUserInformation(final String sub, final String token) {
        final MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_FORM_URLENCODED);
        headers.setBearerAuth(token);

        final HttpEntity<MultiValueMap<String, String>> userInformationRequest =
                new HttpEntity<>(map, headers);
        try {
            return restTemplate.postForObject(
                    userInfoUri, userInformationRequest, UserInformationDetails.class);
        } catch (HttpClientErrorException.Unauthorized e) {
            throw new AccessDeniedException(
                    "Calling user-info endpoint with unauthorized/expired credentials");
        }
    }
}
