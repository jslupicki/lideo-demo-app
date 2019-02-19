package com.slupicki.lideo.testTools;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Optional;

@Component
public class RestTool {

    private final Logger log = LoggerFactory.getLogger(RestTool.class);

    public static final Map<String, String> EMPTY_PARAMS = Maps.newHashMap();
    public static final String BASE_TEMPLATE = "{protocol}://{host}:{port}{path}";
    public static final String CHECK_LOGIN = "{protocol}://{host}:{port}/client/login/{login}";

    private final RestTemplate restTemplate;
    private final String serverPort;

    public HttpStatus statusCode;
    public HttpHeaders headers;
    public String responseBody;

    public RestTool(
            RestTemplate restTemplate,
            @Value("${server.port}") String serverPort
    ) {
        this.restTemplate = restTemplate;
        this.serverPort = serverPort;
        this.restTemplate.setErrorHandler(
                new ResponseErrorHandler() {
                    @Override
                    public boolean hasError(ClientHttpResponse response) throws IOException {
                        //log.error("hasError in RestTemplate: {}", response.getStatusText());
                        return false;
                    }

                    @Override
                    public void handleError(ClientHttpResponse response) throws IOException {
                        //log.error("handleError in RestTemplate: {}", response.getStatusText());
                    }
                }
        );
    }

    public <T> Optional<T> get(String service, Class<T> clazz, Map<String, String> queryParams, Map<String, String> uriParams) throws Exception {
        return call(HttpMethod.GET, service, clazz, null, queryParams, uriParams);
    }

    public <T> Optional<T> get(String service, TypeReference<T> typeReference, Map<String, String> queryParams, Map<String, String> uriParams)
            throws Exception {
        return call(HttpMethod.GET, service, typeReference, null, queryParams, uriParams);
    }

    public <T> Optional<T> post(String service, Class<T> clazz, Object data, Map<String, String> queryParams, Map<String, String> uriParams)
            throws Exception {
        return call(HttpMethod.POST, service, clazz, data, queryParams, uriParams);
    }

    public <T> Optional<T> post(String service, TypeReference<T> typeReference, Object data, Map<String, String> queryParams,
                                Map<String, String> uriParams)
            throws Exception {
        return call(HttpMethod.POST, service, typeReference, data, queryParams, uriParams);
    }

    public <T> Optional<T> put(String service, Class<T> clazz, Object data, Map<String, String> queryParams, Map<String, String> uriParams)
            throws Exception {
        return call(HttpMethod.PUT, service, clazz, data, queryParams, uriParams);
    }

    public <T> Optional<T> put(String service, TypeReference<T> typeReference, Object data, Map<String, String> queryParams,
                               Map<String, String> uriParams)
            throws Exception {
        return call(HttpMethod.PUT, service, typeReference, data, queryParams, uriParams);
    }

    public <T> Optional<T> patch(String service, Class<T> clazz, Object data, Map<String, String> queryParams, Map<String, String> uriParams)
            throws Exception {
        return call(HttpMethod.PATCH, service, clazz, data, queryParams, uriParams);
    }

    public <T> Optional<T> patch(String service, TypeReference<T> typeReference, Object data, Map<String, String> queryParams,
                                 Map<String, String> uriParams)
            throws Exception {
        return call(HttpMethod.PATCH, service, typeReference, data, queryParams, uriParams);
    }

    public <T> Optional<T> delete(String service, Class<T> clazz, Map<String, String> queryParams, Map<String, String> uriParams)
            throws Exception {
        return call(HttpMethod.DELETE, service, clazz, null, queryParams, uriParams);
    }

    public <T> Optional<T> delete(String service, TypeReference<T> typeReference, Map<String, String> queryParams,
                                  Map<String, String> uriParams)
            throws Exception {
        return call(HttpMethod.DELETE, service, typeReference, null, queryParams, uriParams);
    }

    private <T> Optional<T> call(HttpMethod type, String service, Class<T> clazz, Object data, Map<String, String> queryParams, Map<String, String> uriParams)
            throws Exception {
        TypeReference<T> typeReference = new TypeReference<T>() {
            @Override
            public Type getType() {
                return TypeToken.of(clazz).getType();
            }
        };
        return call(type, service, typeReference, data, queryParams, uriParams);
    }

    private <T> Optional<T> call(HttpMethod type, String service, TypeReference<T> typeReference, Object data, Map<String,
            String> queryParams, Map<String, String> uriParams)
            throws Exception {
        String url = prepareUrl(service, queryParams, uriParams);
        String body = data == null ? null : data instanceof String ? (String) data : JSON.MAPPER.writeValueAsString(data);
        String msg = String.format("call: %s '%s' and body: %s", type.toString(), url, body);

        try {
            if (headers == null) {
                headers = new HttpHeaders();
            }
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, type, requestEntity, String.class);
            responseBody = responseEntity.getBody();
            statusCode = responseEntity.getStatusCode();
            log.debug("{} -> got status {} and body: {}", msg, statusCode, responseBody);
            if (statusCode != HttpStatus.OK || responseBody == null) {
                return Optional.empty();
            }
            if (typeReference.getType().equals(String.class)) {
                return Optional.ofNullable((T) responseBody);
            }
            log.debug("Successful {}", msg);
            T result = JSON.MAPPER.readValue(responseBody, typeReference);
            return Optional.ofNullable(result);
        } catch (Throwable e) {
            log.error("Failed {}", msg, e);
            throw e;
        }
    }

    private String prepareUrl(String service, Map<String, String> queryParams, Map<String, String> uriParams) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(service);
        queryParams.forEach(uriComponentsBuilder::queryParam);
        uriComponentsBuilder.host("localhost");
        uriComponentsBuilder.port(serverPort);
        Map<String, String> fullUriParams = Maps.newHashMap(uriParams);
        fullUriParams.putIfAbsent("protocol", "http");
        UriComponents uriComponents = uriComponentsBuilder.buildAndExpand(fullUriParams);
        return uriComponents.toUriString();
    }
}
