package com.codepractice.submission_service.service;

import com.codepractice.submission_service.enums.ExecuteType;
import com.codepractice.submission_service.model.dto.internal.request.SubmissionRequest;
import com.codepractice.submission_service.model.dto.internal.response.SubmissionReponse;

public interface SubmissionService {
    public SubmissionReponse execute(SubmissionRequest solutions, ExecuteType type);
}
