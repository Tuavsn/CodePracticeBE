package com.codepractice.submission_service.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.codepractice.submission_service.model.dto.request.Judge0Request;
import com.codepractice.submission_service.model.dto.response.Judge0Response;
import com.codepractice.submission_service.service.RestTemplateService;
import com.codepractice.submission_service.service.SubmitStrategy;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AsynchronousSubmit implements SubmitStrategy {
    private final RestTemplateService restTemplateService;

    @Override
    public List<Judge0Response> execute(List<Judge0Request> requests, Map<String, String> params) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'execute'");
    }
    
}
