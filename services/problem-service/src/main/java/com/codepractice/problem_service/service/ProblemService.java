package com.codepractice.problem_service.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.codepractice.problem_service.enums.ProblemDifficulty;
import com.codepractice.problem_service.model.dto.internal.ProblemClientTestCaseResponse;
import com.codepractice.problem_service.model.dto.internal.UpdateProblemStatsRequest;
import com.codepractice.problem_service.model.dto.request.ProblemRequest;
import com.codepractice.problem_service.model.dto.response.ProblemResponse;

public interface ProblemService {
    public Page<ProblemResponse> getAll(String title, List<String> tags, ProblemDifficulty difficulty,
            Pageable pageable);

    public ProblemResponse getById(String id);

    public ProblemClientTestCaseResponse getTestCaseByProblemId(String id);

    public ProblemResponse save(ProblemRequest request);

    public ProblemResponse update(String id, ProblemRequest request);

    public void delete(String id);

    public void hardDelete(String id);

    public void updateProblemStats(UpdateProblemStatsRequest request);
}
