package com.codepractice.forum_service.service;

import java.util.List;

import com.codepractice.forum_service.model.dto.request.CommentRequest;
import com.codepractice.forum_service.model.dto.response.CommentResponse;

public interface CommentService {
    public CommentResponse save(CommentRequest request);

    public CommentResponse update(String id, CommentRequest request);

    public void delete(String id);

    public void hardDelete(String id);

    public List<CommentResponse> getAllByPostId(String id);

    public List<CommentResponse> getAllByUserId(String id);

    public List<CommentResponse> getAllPersonalComments();
}
