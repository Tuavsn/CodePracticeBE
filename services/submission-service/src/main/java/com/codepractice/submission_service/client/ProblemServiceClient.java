package com.codepractice.submission_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.codepractice.submission_service.model.dto.internal.ProblemClientTestCaseResponse;
import com.codepractice.submission_service.model.dto.internal.UpdateProblemStatsRequest;

@FeignClient("problem-service")
public interface ProblemServiceClient {
    @GetMapping("/problems/internal/{id}")
    ProblemClientTestCaseResponse getProblemById(@PathVariable String id);

    @PostMapping("/problems/internal/update-stats")
    void updateProblemStats(@RequestBody UpdateProblemStatsRequest request);
}
