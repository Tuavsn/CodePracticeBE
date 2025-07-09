package com.codepractice.submission_service.service;

import java.util.List;

import com.codepractice.submission_service.enums.ExecuteType;
import com.codepractice.submission_service.model.dto.internal.request.SubmissionRequest;
import com.codepractice.submission_service.model.dto.internal.response.ResultResponse;
import com.codepractice.submission_service.model.dto.internal.response.SubmissionResponse;

public interface SubmissionService {
        public SubmissionResponse execute(SubmissionRequest solutions, ExecuteType type);

    public List<SubmissionResponse> getSubmissions(String userId, String problemId);

    public List<ResultResponse> getResultBySubmissionId(String submissionId);
}
