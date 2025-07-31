package com.codepractice.submission_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codepractice.common_lib.response.ApiResponse;
import com.codepractice.submission_service.enums.ExecuteType;
import com.codepractice.submission_service.model.dto.internal.request.SubmissionRequest;
import com.codepractice.submission_service.model.dto.internal.response.ResultResponse;
import com.codepractice.submission_service.model.dto.internal.response.SubmissionResponse;
import com.codepractice.submission_service.service.SubmissionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("submissions")
@RequiredArgsConstructor
public class SubmissionController {
    private final SubmissionService submissionService;

    @PostMapping("/execute")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<SubmissionResponse>> execute(@RequestBody SubmissionRequest solutions,
            @RequestParam ExecuteType type) {
        SubmissionResponse result = submissionService.execute(solutions, type);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(result, "Executed successfully", HttpStatus.OK.value()));
    }

    @GetMapping()
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<SubmissionResponse>>> getSubmissions(@RequestParam String problemId) {
        List<SubmissionResponse> submissions = submissionService.getSubmissions(problemId);
        return ResponseEntity.ok(ApiResponse.success(
                submissions,
                "Problems retrieved successfully",
                HttpStatus.OK.value()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<ResultResponse>>> getResultBySubmissionId(@PathVariable String id) {
        List<ResultResponse> results = submissionService.getResultBySubmissionId(id);
        return ResponseEntity.ok(ApiResponse.success(
                results,
                "Results retrieved successfully",
                HttpStatus.OK.value()));
    }
}
