package com.codepractice.submission_service.service;

import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;

public interface RestTemplateService {
    public <T> T get(String url, Map<String, String> params, ParameterizedTypeReference<T> responseType);

    public <T> T post(String url, Object requestBody, Map<String, String> params, ParameterizedTypeReference<T> responseType);

    public <T> T put(String url, Object requestBody, Map<String, String> params, ParameterizedTypeReference<T> responseType);

    public void delete(String url, Map<String, String> params);
}
