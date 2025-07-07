package com.codepractice.problem_service.controller;

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
import com.codepractice.problem_service.model.dto.internal.request.ProblemRequest;
import com.codepractice.problem_service.model.dto.internal.response.ProblemResponse;
import com.codepractice.problem_service.service.ProblemService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("problems")
@RequiredArgsConstructor
public class ProblemInterfaceController {
    private final ProblemService problemService;

    @PostMapping
    public ResponseEntity<ApiResponse<ProblemResponse>> createProblem(@RequestBody ProblemRequest problem) {
        ProblemResponse savedProblem = problemService.save(problem);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(savedProblem, "Problem created successfully", HttpStatus.CREATED.value()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProblemResponse>> updateProblem(
            @PathVariable String id,
            @RequestBody ProblemRequest problem
    ) {
        ProblemResponse updatedProblem = problemService.update(id, problem);
        return ResponseEntity.ok(ApiResponse.success(updatedProblem, "Problem updated successfully", HttpStatus.OK.value()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteProblem(@PathVariable String id) {
        problemService.hardDelete(id);
        return ResponseEntity.ok(ApiResponse.success(
                "Problem deleted successfully",
                "Problem deleted successfully",
                HttpStatus.OK.value()
        ));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProblemResponse>>> getAllProblems() {
        List<ProblemResponse> problems = problemService.getAll();
        return ResponseEntity.ok(ApiResponse.success(
                problems,
                "Problems retrieved successfully",
                HttpStatus.OK.value()
        ));
    }

    @GetMapping("/{id}")
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
