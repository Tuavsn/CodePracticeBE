package com.codepractice.forum_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codepractice.common_lib.response.ApiResponse;
import com.codepractice.forum_service.model.dto.internal.request.CommentRequest;
import com.codepractice.forum_service.model.dto.internal.response.CommentResponse;
import com.codepractice.forum_service.service.CommentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(
            @RequestBody CommentRequest request) {
        CommentResponse created = commentService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Comment created successfully", HttpStatus.CREATED.value()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CommentResponse>> updateComment(
            @PathVariable String id,
            @RequestBody CommentRequest request) {
        CommentResponse updated = commentService.update(id, request);
        return ResponseEntity.ok(
                ApiResponse.success(updated, "Comment updated successfully", HttpStatus.OK.value()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteComment(@PathVariable String id) {
        commentService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(
                "Comment deleted successfully", "Comment deleted successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getCommentsByPost(
            @PathVariable("postId") String postId) {
        List<CommentResponse> comments = commentService.getAllByPostId(postId);
        return ResponseEntity.ok(
                ApiResponse.success(comments, "Comments for post retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getCommentsByUser(
            @PathVariable("userId") String userId) {
        List<CommentResponse> comments = commentService.getAllByUserId(userId);
        return ResponseEntity.ok(
                ApiResponse.success(comments, "Comments for user retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/personal")
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getPersonalComments() {
        List<CommentResponse> comments = commentService.getAllPersonalComments();
        return ResponseEntity.ok(
                ApiResponse.success(comments, "Personal comments retrieved successfully", HttpStatus.OK.value()));
    }
}
