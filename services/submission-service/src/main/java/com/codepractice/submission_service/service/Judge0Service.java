package com.codepractice.submission_service.service;

import com.codepractice.submission_service.model.dto.request.ExecuteRequest;
import com.codepractice.submission_service.model.dto.response.Judge0Response;

public interface Judge0Service {
    public Judge0Response execute(ExecuteRequest request);
}
