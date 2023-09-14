/* Decathlon (C)2023 */
package com.decathlon.rest.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
@Slf4j
public class UrlRestHandler {

    public static final String PARAM_PAGE = "page";
    public static final String PARAM_PAGE_SIZE = "size";
    public static final String PARAM_GLOBAL_FILTER = "globalFilter";

    private final String restScheme;
    private final String restHost;
    private final String restPort;
    private final String restContext;

    public Map<String, Object> buildFilterParams(Map<String, Object> filters) {
        Map<String, Object> params = new HashMap<>();

        if (filters != null && !filters.isEmpty()) {
            for (Map.Entry<String, Object> entry : filters.entrySet()) {
                params.put(entry.getKey(), entry.getValue());
            }
        }

        return params;
    }

    public Map<String, Object> buildFilterParams(
            int first,
            int pageSize,
            String sortField,
            String sortOrder,
            Map<String, Object> filters) {
        Map<String, Object> params = new HashMap<>();
        params.put(PARAM_PAGE, (first / pageSize));
        params.put(PARAM_PAGE_SIZE, pageSize);

        if (StringUtils.isNotBlank(sortField)) {
            params.put("sort", sortField);
        }

        if (StringUtils.isNotBlank(sortOrder) && StringUtils.isNotBlank(sortField)) {
            params.put(
                    "sort",
                    params.get("sort")
                            + ","
                            + ("ASCENDING".equalsIgnoreCase(sortOrder) ? "asc" : "desc"));
        }

        if (filters != null && !filters.isEmpty()) {
            params.putAll(buildFilterParams(filters));
        }

        log.debug(
                "Passing pagination values page:{} pageSize:{} sort:{} sortOrder:{}",
                params.get(PARAM_PAGE),
                pageSize,
                sortField,
                sortOrder);

        return params;
    }

    public String buildEncodedURI(String path) {
        return buildURI(path, null, null, true);
    }

    public String buildEncodedURI(String path, Map<String, Object> params) {
        return buildURI(path, params, null, true);
    }

    public String buildURI(String path) {
        return buildURI(path, null, null, false);
    }

    public String buildURI(String path, Map<String, Object> params) {
        return buildURI(path, params, null, false);
    }

    public String buildURI(
            String path, Map<String, Object> queryParams, Map<String, String> pathParams) {
        return buildURI(path, queryParams, pathParams, false);
    }

    public String buildURI(
            String path,
            Map<String, Object> queryParams,
            Map<String, String> pathParams,
            boolean encoding) {
        UriComponentsBuilder uriComponentsBuilder =
                UriComponentsBuilder.newInstance()
                        .scheme(getRestScheme())
                        .host(getRestHost())
                        .port(getRestPort())
                        .path(getRestContext() + path);

        if (queryParams != null && !queryParams.isEmpty()) {
            for (Map.Entry<String, Object> entry : queryParams.entrySet()) {
                uriComponentsBuilder.queryParam(entry.getKey(), entry.getValue());
            }
        }

        if (pathParams != null && !pathParams.isEmpty() && encoding) {
            return uriComponentsBuilder.buildAndExpand(pathParams).encode().toUriString();
        } else if (pathParams != null && !pathParams.isEmpty() && !encoding) {
            return uriComponentsBuilder.buildAndExpand(pathParams).toUriString();
        }

        if (encoding) {
            return uriComponentsBuilder.build().encode().toUriString();
        }

        return uriComponentsBuilder.build().toUriString();
    }
}
