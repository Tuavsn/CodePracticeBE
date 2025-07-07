package com.codepractice.problem_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codepractice.common_lib.response.ApiResponse;
import com.codepractice.problem_service.model.dto.internal.response.ProblemResponse;
import com.codepractice.problem_service.service.ProblemService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("problems")
@RequiredArgsConstructor
public class ProblemInternalController {
    private final ProblemService problemService;

    @GetMapping("/internal/{id}")
    public ResponseEntity<ApiResponse<ProblemResponse>> getProblemById(@PathVariable String id) {
        ProblemResponse problem = problemService.getById(id);
        if (problem != null) {
            return ResponseEntity.ok(ApiResponse.success(problem, "Problem found", HttpStatus.OK.value()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Problem not found", HttpStatus.NOT_FOUND.value()));
        }
    }
}
