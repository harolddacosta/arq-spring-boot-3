/* Decathlon (C)2023 */
package com.decathlon.security.test.utils;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class JwtBuilder {

    private static final String LABEL_ROLES = "roles";
    private static final String LABEL_SESSION_STATE = "session_state";
    private static final String LABEL_REALM_ACCESS = "realm_access";
    private static final String LABEL_RESOURCE_ACCESS = "resource_access";
    private static final String LABEL_EMAIL_VERIFIED = "email_verified";
    private static final String LABEL_GIVEN_NAME = "given_name";
    private static final String LABEL_FAMILY_NAME = "family_name";
    private static final String LABEL_USER_NAME = "user_name";
    private static final String LABEL_PREFERRED_USERNAME = "preferred_username";

    private String secretKey;

    private LocalDateTime exp;
    private LocalDateTime iat;
    private String jti;
    private String iss;
    private String aud;
    private String sub;
    private String typ;
    private String azp;
    private String sessionState;
    private String acr;
    private final Map<String, Object> realmAccessRoles = new HashMap<>();
    private Map<String, Object> resourceAccessRoles = new HashMap<>();
    private String[] audienceRoles;
    private String[] authorizedRoles;
    private String[] scope;
    private String sid;
    private boolean emailVerified;
    private String userName;
    private String name;
    private String preferredUsername;
    private String givenName;
    private String familyName;
    private String countryCode;
    private Long rootCenterId;
    private Long[] centersId;

    private final Map<String, Object> payloadClaims;

    private JwtBuilder() {
        // Builder class
        payloadClaims = new HashMap<>();

        this.exp(2);
        this.iat(0);
        this.jti(UUID.randomUUID().toString());
        this.iss("http://localhost:9090/auth/realms/dev");
        this.aud("account");
        this.sub(UUID.randomUUID().toString());
        this.typ("Bearer");
        this.azp("employee-service");
        this.sessionState(UUID.randomUUID().toString());
        this.acr("1");

        this.realmAccessRoles(
                new String[] {"default-roles-dev", "offline_access", "uma_authorization"});

        resourceAccessRoles = new HashMap<>();
        this.audienceRoles(new String[] {"manage-account", "manage-account-links", "view-profile"});
        this.authorizeRoles(new String[] {"USER"});

        this.scope(new String[] {"email", "profile"});
        this.emailVerified(false);
        this.userName("rachel");
        this.givenName("Rachel");
        this.familyName("White");
        this.countryCode("ES");
        this.rootCenterId(1L);
        this.centersId(new Long[] {1L, 2L, 3L});
    }

    public static JwtBuilder getInstance() {
        return new JwtBuilder();
    }

    public JwtBuilder secretKey(String secretKey) {
        this.secretKey = secretKey;
        return this;
    }

    public JwtBuilder exp(int hoursSinceNowToExpiration) {
        if (hoursSinceNowToExpiration >= 0) {
            this.exp = LocalDateTime.now().plusHours(hoursSinceNowToExpiration);

        } else {
            this.exp = LocalDateTime.now().minusHours(Math.abs(hoursSinceNowToExpiration));
            this.iat = this.exp;
        }

        return this;
    }

    public JwtBuilder iat(int hoursSinceNowToIssued) {
        if (hoursSinceNowToIssued >= 0) {
            this.iat = LocalDateTime.now().plusHours(hoursSinceNowToIssued);

        } else {
            this.iat = LocalDateTime.now().minusHours(Math.abs(hoursSinceNowToIssued));
        }

        return this;
    }

    public JwtBuilder jti(String jti) {
        this.jti = jti;
        return this;
    }

    public JwtBuilder iss(String iss) {
        this.iss = iss;
        return this;
    }

    public JwtBuilder aud(String aud) {
        this.aud = aud;
        return this;
    }

    public JwtBuilder sub(String sub) {
        this.sub = sub;
        return this;
    }

    public JwtBuilder typ(String typ) {
        this.typ = typ;
        return this;
    }

    public JwtBuilder azp(String azp) {
        this.azp = azp;
        return this;
    }

    public JwtBuilder sessionState(String sessionState) {
        this.sessionState = sessionState;
        return this;
    }

    public JwtBuilder acr(String acr) {
        this.acr = acr;
        return this;
    }

    public JwtBuilder realmAccessRoles(String[] roles) {
        realmAccessRoles.put(LABEL_ROLES, roles);
        return this;
    }

    public JwtBuilder audienceRoles(String[] roles) {
        this.audienceRoles = roles;
        return this;
    }

    public JwtBuilder authorizeRoles(String[] roles) {
        this.authorizedRoles = roles;
        return this;
    }

    public JwtBuilder scope(String[] scope) {
        this.scope = scope;
        return this;
    }

    public JwtBuilder emailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
        return this;
    }

    public JwtBuilder userName(String userName) {
        this.userName = userName;
        return this;
    }

    public JwtBuilder givenName(String givenName) {
        this.givenName = givenName;
        return this;
    }

    public JwtBuilder familyName(String familyName) {
        this.familyName = familyName;
        return this;
    }

    public JwtBuilder countryCode(String countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    public JwtBuilder rootCenterId(Long rootCenterId) {
        this.rootCenterId = rootCenterId;
        return this;
    }

    public JwtBuilder centersId(Long[] centersId) {
        this.centersId = centersId;
        return this;
    }

    public JwtBuilder addCustomClaim(String claim, Object value) {
        payloadClaims.put(claim, value);
        return this;
    }

    public JwtBuilder addCustomClaim(String claim, String value) {
        payloadClaims.put(claim, value);
        return this;
    }

    @SuppressWarnings("unchecked")
    public String build() {
        try {
            JWSSigner signer = new MACSigner(secretKey);

            sid = sessionState; // No seteable
            preferredUsername = userName; // No seteable
            name = givenName + " " + familyName; // No seteable

            payloadClaims.put("typ", typ);
            payloadClaims.put("azp", azp);
            payloadClaims.put(LABEL_SESSION_STATE, sessionState);
            payloadClaims.put("acr", acr);
            payloadClaims.put(LABEL_REALM_ACCESS, realmAccessRoles);
            payloadClaims.put("scope", Stream.of(scope).collect(Collectors.joining(" ")));
            payloadClaims.put("sid", sid);
            payloadClaims.put(LABEL_EMAIL_VERIFIED, emailVerified);
            payloadClaims.put(LABEL_USER_NAME, userName);
            payloadClaims.put("name", name);
            payloadClaims.put(LABEL_PREFERRED_USERNAME, preferredUsername);
            payloadClaims.put(LABEL_GIVEN_NAME, givenName);
            payloadClaims.put(LABEL_FAMILY_NAME, familyName);
            payloadClaims.put("country_code", countryCode);
            payloadClaims.put("root_center_id", rootCenterId);
            payloadClaims.put("centers_ids", centersId);
            payloadClaims.put("exp", this.exp.toEpochSecond(ZoneOffset.UTC));
            payloadClaims.put("iat", this.iat.toEpochSecond(ZoneOffset.UTC));

            resourceAccessRoles.put(aud, new HashMap<>());
            resourceAccessRoles.put(azp, new HashMap<>());
            ((Map<String, Object>) resourceAccessRoles.get(aud)).put(LABEL_ROLES, audienceRoles);
            ((Map<String, Object>) resourceAccessRoles.get(azp)).put(LABEL_ROLES, authorizedRoles);
            payloadClaims.put(LABEL_RESOURCE_ACCESS, resourceAccessRoles);

            JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder();
            builder.issuer(iss);
            builder.subject(sub);
            builder.jwtID(jti);
            builder.audience(List.of(aud));

            JWTClaimsSet claimsSet = builder.build();
            JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

            payloadClaims.putAll(claimsSet.getClaims());
            Payload payload = new Payload(payloadClaims);

            JWSObject jwsObject = new JWSObject(header, payload);

            jwsObject.sign(signer);

            String token = jwsObject.serialize();

            log.trace("JWT generated:{}", token);

            return token;
        } catch (JOSEException ex) {
            log.error("Problem creating jwt", ex);

            return null;
        }
    }
}
