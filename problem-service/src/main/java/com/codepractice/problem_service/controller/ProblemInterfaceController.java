package com.codepractice.problem_service.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import com.codepractice.problem_service.enums.ProblemDifficulty;
import com.codepractice.problem_service.enums.ReactionType;
import com.codepractice.problem_service.model.dto.internal.request.CommentRequest;
import com.codepractice.problem_service.model.dto.internal.request.ProblemRequest;
import com.codepractice.problem_service.model.dto.internal.response.CommentResponse;
import com.codepractice.problem_service.model.dto.internal.response.ProblemResponse;
import com.codepractice.problem_service.service.CommentService;
import com.codepractice.problem_service.service.ProblemService;
import com.codepractice.problem_service.service.ReactionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("problems")
@RequiredArgsConstructor
public class ProblemInterfaceController {
    private final ProblemService problemService;
    private final CommentService commentService;
    private final ReactionService reactionService;

    // ======================== PROBLEM CRUD OPERATIONS ========================
    @GetMapping
    @Operation(summary = "Get all problems with pagination and filtering")
    public ResponseEntity<ApiResponse<Page<ProblemResponse>>> getAllProblems(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort by field") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "desc") String sortDir,
            @Parameter(description = "Search by title") @RequestParam(required = false) String title,
            @Parameter(description = "Filter by topic") @RequestParam(required = false) List<String> tags,
            @Parameter(description = "Filter by difficulty") @RequestParam(required = false) ProblemDifficulty difficulty) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ProblemResponse> problems = problemService.getAll(title, tags, difficulty, pageable);
        return ResponseEntity.ok(ApiResponse.success(
                problems,
                "Problems retrieved successfully",
                HttpStatus.OK.value()));
    }

    @GetMapping("/{problemId}")
    @Operation(summary = "Get a specific problem by ID")
    public ResponseEntity<ApiResponse<ProblemResponse>> getProblemById(@PathVariable String problemId) {
        ProblemResponse problem = problemService.getById(problemId);
        if (problem != null) {
            return ResponseEntity.ok(ApiResponse.success(problem, "Problem found", HttpStatus.OK.value()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Problem not found", HttpStatus.NOT_FOUND.value()));
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new problem")
    public ResponseEntity<ApiResponse<ProblemResponse>> createProblem(@RequestBody ProblemRequest problem) {
        ProblemResponse savedProblem = problemService.save(problem);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(savedProblem, "Problem created successfully", HttpStatus.CREATED.value()));
    }

    @PutMapping("/{problemId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a problem")
    public ResponseEntity<ApiResponse<ProblemResponse>> updateProblem(
            @PathVariable String problemId,
            @RequestBody ProblemRequest problem) {
        ProblemResponse updatedProblem = problemService.update(problemId, problem);
        return ResponseEntity
                .ok(ApiResponse.success(updatedProblem, "Problem updated successfully", HttpStatus.OK.value()));
    }

    @DeleteMapping("/{problemId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a problem")
    public ResponseEntity<ApiResponse<String>> deleteProblem(@PathVariable String problemId) {
        problemService.hardDelete(problemId);
        return ResponseEntity.ok(ApiResponse.success(
                "Problem deleted successfully",
                "Problem deleted successfully",
                HttpStatus.OK.value()));
    }

    // ======================== REACTION OPERATIONS ========================
    @PostMapping("/{problemId}/reactions")
    @Operation(summary = "Reaction a problem")
    public ResponseEntity<ApiResponse<ProblemResponse>> addOrUpdateReaction(
            @PathVariable String problemId,
            @RequestParam ReactionType type) {
        ProblemResponse problem = reactionService.addOrUpdateReaction(problemId, type);
        return ResponseEntity.ok(
                ApiResponse.success(problem, "Reaction for problem created successfully", HttpStatus.OK.value()));
    }

    // ======================== COMMENT OPERATIONS ========================
    @GetMapping("/{problemId}/comments")
    @Operation(summary = "Get comment by problem")
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getCommentsByProblem(
            @PathVariable String problemId) {
        List<CommentResponse> comments = commentService.getAllByProblemId(problemId);
        return ResponseEntity.ok(
                ApiResponse.success(comments, "Comments for post retrieved successfully", HttpStatus.OK.value()));
    }

    @PostMapping("/{problemId}/comments")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Create a new comment")
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(
            @RequestBody CommentRequest request) {
        CommentResponse created = commentService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Comment created successfully", HttpStatus.CREATED.value()));
    }

    @PutMapping("/{problemId}/comments/{commentId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Update a comment")
    public ResponseEntity<ApiResponse<CommentResponse>> updateComment(
            @PathVariable String commentId,
            @RequestBody CommentRequest request) {
        CommentResponse updated = commentService.update(commentId, request);
        return ResponseEntity.ok(
                ApiResponse.success(updated, "Comment updated successfully", HttpStatus.OK.value()));
    }

    @DeleteMapping("/{problemId}/comments/{commentId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Delete a comment")
    public ResponseEntity<ApiResponse<String>> deleteComment(@PathVariable String commentId) {
        commentService.delete(commentId);
        return ResponseEntity.ok(ApiResponse.success(
                "Comment deleted successfully", "Comment deleted successfully", HttpStatus.OK.value()));
    }
}
