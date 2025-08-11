package com.codepractice.problem_service.controller.internal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codepractice.problem_service.model.dto.internal.ProblemClientTestCaseResponse;
import com.codepractice.problem_service.service.ProblemService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("problems")
@RequiredArgsConstructor
public class ProblemInternalController {
    private final ProblemService problemService;

    @GetMapping("/internal/{id}")
    public ResponseEntity<ProblemClientTestCaseResponse> getProblemById(@PathVariable String id) {
        ProblemClientTestCaseResponse problem = problemService.getTestCaseByProblemId(id);
        if (problem != null) {
            return ResponseEntity.ok(problem);
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
