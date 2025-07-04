package com.codepractice.problem_service.service;

import java.util.List;

import com.codepractice.problem_service.model.dto.internal.request.CommentRequest;
import com.codepractice.problem_service.model.dto.internal.response.CommentResponse;

public interface CommentService {
    public CommentResponse save(CommentRequest request);

    public CommentResponse update(String id, CommentRequest request);

    public void delete(String id);

    public void hardDelete(String id);

    public List<CommentResponse> getAllByProblemId(String id);

    public List<CommentResponse> getAllByUserId(String id);
}
