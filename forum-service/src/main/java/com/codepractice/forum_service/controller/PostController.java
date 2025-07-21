package com.codepractice.forum_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codepractice.common_lib.response.ApiResponse;
import com.codepractice.forum_service.model.dto.internal.request.PostRequest;
import com.codepractice.forum_service.model.dto.internal.response.PostResponse;
import com.codepractice.forum_service.service.PostService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<PostResponse>> createPost(@RequestBody PostRequest post) {
        PostResponse savedPost = postService.save(post);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(savedPost, "Post created successfully", HttpStatus.CREATED.value()));
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<PostResponse>> updatePost(
        @PathVariable String id,
        @RequestBody PostRequest post
    ) {
        PostResponse updatedPost = postService.update(id, post);
        return ResponseEntity.ok(ApiResponse.success(updatedPost, "Post updated successfully", HttpStatus.OK.value()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<String>> deletePost(@PathVariable String id) {
        postService.hardDelete(id);
        return ResponseEntity
                .ok(ApiResponse.success("Post deleted successfully", "Post deleted successfully",
                        HttpStatus.OK.value()));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PostResponse>>> getAllPosts() {
        List<PostResponse> posts = postService.getAll();
        return ResponseEntity.ok(ApiResponse.success(posts, "Posts retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> getPostById(@PathVariable String id) {
        PostResponse post = postService.getById(id);
        if (post != null) {
            return ResponseEntity.ok(ApiResponse.success(post, "Post found", HttpStatus.OK.value()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Post not found", HttpStatus.NOT_FOUND.value()));
        }
    }
}
