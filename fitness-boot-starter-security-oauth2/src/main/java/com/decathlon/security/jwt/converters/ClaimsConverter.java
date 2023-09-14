/* Decathlon (C)2023 */
package com.decathlon.security.jwt.converters;

import org.springframework.core.convert.converter.Converter;

import java.util.Map;

public interface ClaimsConverter extends Converter<Map<String, Object>, Map<String, Object>> {}
