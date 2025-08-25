package com.codepractice.submission_service.service;

import java.util.List;

import com.codepractice.submission_service.model.dto.request.RunRequest;
import com.codepractice.submission_service.model.dto.request.SubmissionRequest;
import com.codepractice.submission_service.model.dto.response.ResultResponse;
import com.codepractice.submission_service.model.dto.response.RunResponse;
import com.codepractice.submission_service.model.dto.response.SubmissionResponse;

public interface SubmissionService {
    public RunResponse runSolution(RunRequest solution);

    public SubmissionResponse submitSolution(SubmissionRequest solutions);

    public List<SubmissionResponse> getSubmissions(String problemId);

    public List<ResultResponse> getResultBySubmissionId(String submissionId);
}
