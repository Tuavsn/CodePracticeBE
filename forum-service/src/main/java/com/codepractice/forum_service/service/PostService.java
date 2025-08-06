package com.codepractice.forum_service.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.codepractice.forum_service.model.dto.internal.request.PostRequest;
import com.codepractice.forum_service.model.dto.internal.response.PostResponse;

public interface PostService {
    public Page<PostResponse> getAll(String title, List<String> topic, Long authorId, Pageable pageable);

    public PostResponse getById(String id);

    public PostResponse save(PostRequest request);

    public PostResponse update(String id, PostRequest request);

    public void delete(String id);

    public void hardDelete(String id);
}
