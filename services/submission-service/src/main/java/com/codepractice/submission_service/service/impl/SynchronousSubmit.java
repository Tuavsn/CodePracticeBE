package com.codepractice.submission_service.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import com.codepractice.submission_service.constants.Judge0Configurations;
import com.codepractice.submission_service.constants.Judge0Endpoint;
import com.codepractice.submission_service.model.dto.request.Judge0Request;
import com.codepractice.submission_service.model.dto.response.Judge0Response;
import com.codepractice.submission_service.service.RestTemplateService;
import com.codepractice.submission_service.service.SubmitStrategy;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class SynchronousSubmit implements SubmitStrategy {
    private final RestTemplateService restTemplateService;

    @Override
    public List<Judge0Response> execute(List<Judge0Request> requests, Map<String, String> params) {
        Map<String, String> submitParams = new HashMap<>(params);

        submitParams.put(Judge0Configurations.RESULT_WAIT, "true");
        ParameterizedTypeReference<Judge0Response> typeRef = new ParameterizedTypeReference<Judge0Response>() {
        };

        List<Judge0Response> judge0Results = requests.stream().map(request -> restTemplateService.post(
                Judge0Endpoint.SUBMISSION_ENDPOINT,
                request,
                submitParams,
                typeRef)).toList();

        return judge0Results;
    }
}
