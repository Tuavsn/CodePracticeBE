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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codepractice.common_lib.response.ApiResponse;
import com.codepractice.forum_service.enums.ReactionType;
import com.codepractice.forum_service.model.dto.internal.request.CommentRequest;
import com.codepractice.forum_service.model.dto.internal.request.PostRequest;
import com.codepractice.forum_service.model.dto.internal.response.CommentResponse;
import com.codepractice.forum_service.model.dto.internal.response.PostResponse;
import com.codepractice.forum_service.service.CommentService;
import com.codepractice.forum_service.service.PostService;
import com.codepractice.forum_service.service.ReactionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final CommentService commentService;
    private final ReactionService reactionService;

    // ======================== POST CRUD OPERATIONS ========================
    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<PostResponse>> createPost(@RequestBody PostRequest post) {
        PostResponse savedPost = postService.save(post);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(savedPost, "Post created successfully", HttpStatus.CREATED.value()));
    }

    @PutMapping("/{postId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<PostResponse>> updatePost(
            @PathVariable String postId,
            @RequestBody PostRequest post) {
        PostResponse updatedPost = postService.update(postId, post);
        return ResponseEntity.ok(ApiResponse.success(updatedPost, "Post updated successfully", HttpStatus.OK.value()));
    }

    @DeleteMapping("/{postId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<String>> deletePost(@PathVariable String postId) {
        postService.hardDelete(postId);
        return ResponseEntity
                .ok(ApiResponse.success("Post deleted successfully", "Post deleted successfully",
                        HttpStatus.OK.value()));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PostResponse>>> getAllPosts() {
        List<PostResponse> posts = postService.getAll();
        return ResponseEntity.ok(ApiResponse.success(posts, "Posts retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostResponse>> getPostById(@PathVariable String postId) {
        PostResponse post = postService.getById(postId);
        return ResponseEntity.ok(ApiResponse.success(post, "Post found", HttpStatus.OK.value()));
    }

    // ======================== REACTION OPERATIONS ========================
    @PostMapping("/{postId}/reactions")
    public ResponseEntity<ApiResponse<PostResponse>> addOrUpdateReaction(
            @PathVariable String postId,
            @RequestParam ReactionType reactionType) {
        PostResponse post = reactionService.addOrUpdateReaction(postId, reactionType);
        return ResponseEntity.ok(
                ApiResponse.success(post, "Reaction for post created successfully", HttpStatus.OK.value()));
    }

    // ======================== COMMENT OPERATIONS ========================
    @GetMapping("/{postId}/comments")
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getCommentsByPost(
            @PathVariable String postId) {
        List<CommentResponse> comments = commentService.getAllByPostId(postId);
        return ResponseEntity.ok(
                ApiResponse.success(comments, "Comments for post retrieved successfully", HttpStatus.OK.value()));
    }

    @PostMapping("/{postId}/comments")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(
            @RequestBody CommentRequest request) {
        CommentResponse created = commentService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Comment created successfully", HttpStatus.CREATED.value()));
    }

    @PutMapping("/{postId}/comments/{commentId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<CommentResponse>> updateComment(
            @PathVariable String commentId,
            @RequestBody CommentRequest request) {
        CommentResponse updated = commentService.update(commentId, request);
        return ResponseEntity.ok(
                ApiResponse.success(updated, "Comment updated successfully", HttpStatus.OK.value()));
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteComment(@PathVariable String commentId) {
        commentService.delete(commentId);
        return ResponseEntity.ok(ApiResponse.success(
                "Comment deleted successfully", "Comment deleted successfully", HttpStatus.OK.value()));
    }
}
