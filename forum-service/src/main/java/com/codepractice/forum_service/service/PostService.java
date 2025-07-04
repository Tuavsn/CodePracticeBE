package com.codepractice.forum_service.service;

import java.util.List;

import com.codepractice.forum_service.model.dto.internal.request.PostRequest;
import com.codepractice.forum_service.model.dto.internal.response.PostResponse;

public interface PostService {
    public PostResponse save(PostRequest request);

    public PostResponse update(String id, PostRequest request);

    public void delete(String id);

    public void hardDelete(String id);

    public List<PostResponse> getAll();

    public List<PostResponse> getByUserId(String id);

    public PostResponse getById(String id);
}
