/* Decathlon (C)2023 */
package com.decathlon.security;

public class SecurityConstants {

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String CLAIM_USER_ID = "user_id";
    public static final String CLAIM_AUTHORITIES = "authorities";
    public static final String CLAIM_USER_NAME = "user_name";
    public static final String CLAIM_ROOT_CENTER_ID = "root_center_id";
    public static final String CLAIM_CENTERS_IDS = "centers_ids";
    public static final String CLAIM_COUNTRY_CODE = "country_code";
    public static final String JWT_DECODED_ATTRIBUTE_NAME = "jwt_decoded";

    private SecurityConstants() {
        // Constant class
    }
}
