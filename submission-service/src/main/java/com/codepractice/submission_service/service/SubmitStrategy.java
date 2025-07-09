package com.codepractice.submission_service.service;

import java.util.List;
import java.util.Map;

import com.codepractice.submission_service.model.dto.internal.request.Judge0Request;
import com.codepractice.submission_service.model.dto.internal.response.Judge0Response;

public interface SubmitStrategy {
    public List<Judge0Response> execute(List<Judge0Request> requests, Map<String, String> params);
}
