package com.codepractice.submission_service.service.impl;

import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.codepractice.submission_service.service.RestTemplateService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class RestTemplateServiceImpl implements RestTemplateService {
    private final RestTemplate restTemplate;

    @Override
    public <T> T get(String url, Map<String, String> params, ParameterizedTypeReference<T> responseType) {
        try {
            HttpEntity<String> entity = new HttpEntity<>(null);
            url = buildUrlWithParams(url, params);
            log.debug(">>> GET URL: {}", url);
            ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.GET, entity, responseType);
            log.info(">>> GET successful. Response: {}", response);
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            log.error(">>> HTTP Error during GET request. Status: {}, Response: {}", ex.getStatusCode(),
                    ex.getResponseBodyAsString());
        } catch (Exception e) {
            log.error(">>> Unexpected error during GET request: {}", e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <T> T post(String url, Object requestBody, Map<String, String> params,
            ParameterizedTypeReference<T> responseType) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-type", "application/json;charset=UTF-8");
            HttpEntity<Object> entity = new HttpEntity<>(requestBody, headers);
            url = buildUrlWithParams(url, params);
            log.debug(">>> POST URL: {}", url);
            ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.POST, entity, responseType);
            log.info(">>> POST successful. Response: {}", response);
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            log.error(">>> HTTP Error during POST request. Status: {}, Response: {}", ex.getStatusCode(),
                    ex.getResponseBodyAsString());
        } catch (Exception e) {
            log.error(">>> Unexpected error during POST request: {}", e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <T> T put(String url, Object requestBody, Map<String, String> params,
            ParameterizedTypeReference<T> responseType) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Object> entity = new HttpEntity<>(requestBody, headers);
            url = buildUrlWithParams(url, params);
            log.debug(">>> PUT URL: {}", url);
            ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.PUT, entity, responseType);
            log.info(">>> PUT successful. Response: {}", response);
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            log.error(">>> HTTP Error during PUT request. Status: {}, Response: {}", ex.getStatusCode(),
                    ex.getResponseBodyAsString());
        } catch (Exception e) {
            log.error(">>> Unexpected error during PUT request: {}", e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void delete(String url, Map<String, String> params) {
        try {
            url = buildUrlWithParams(url, params);
            log.debug(">>> DELETE URL: {}", url);
            restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
            log.info(">>> DELETE successful");
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            log.error(">>> HTTP Error during DELETE request. Status: {}, Response: {}", ex.getStatusCode(),
                    ex.getResponseBodyAsString());
        } catch (Exception e) {
            log.error(">>> Unexpected error during DELETE request: {}", e.getMessage(), e);
            e.printStackTrace();
        }
    }

    private String buildUrlWithParams(String url, Map<String, String> params) {
        StringBuilder requestUrl = new StringBuilder(url);
        if (params != null && !params.isEmpty()) {
            requestUrl.append("?");
            params.forEach((key, value) -> requestUrl.append(key).append("=").append(value).append("&"));
            requestUrl.deleteCharAt(requestUrl.length() - 1);
        }
        return requestUrl.toString();
    }
    
}
