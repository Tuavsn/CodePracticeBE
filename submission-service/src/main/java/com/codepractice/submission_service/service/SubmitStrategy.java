package com.codepractice.submission_service.service;

import java.util.List;
import java.util.Map;

import com.codepractice.submission_service.model.dto.internal.request.Judge0Request;
import com.codepractice.submission_service.model.dto.internal.response.SubmissionReponse;

public interface SubmitStrategy {
    public List<SubmissionReponse> execute(List<Judge0Request> requests, Map<String, String> params);
}
