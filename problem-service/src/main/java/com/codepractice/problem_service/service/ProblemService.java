package com.codepractice.problem_service.service;

import java.util.List;

import com.codepractice.problem_service.model.dto.internal.request.ProblemRequest;
import com.codepractice.problem_service.model.dto.internal.response.ProblemResponse;

public interface ProblemService {
    public ProblemResponse save(ProblemRequest request);

    public ProblemResponse update(String id, ProblemRequest request);

    public void delete(String id);

    public void hardDelete(String id);

    public List<ProblemResponse> getAll();

    public ProblemResponse getById(String id);
}
