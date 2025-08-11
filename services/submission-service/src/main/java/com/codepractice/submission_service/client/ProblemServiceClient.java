package com.codepractice.submission_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.codepractice.submission_service.model.dto.internal.ProblemClientTestCaseResponse;

@FeignClient("problem-service")
public interface ProblemServiceClient {
    @GetMapping("/problems/internal/{id}")
    ProblemClientTestCaseResponse getProblemById(@PathVariable String id);
}
