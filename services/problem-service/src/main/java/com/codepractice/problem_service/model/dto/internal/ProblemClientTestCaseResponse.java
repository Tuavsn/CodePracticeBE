package com.codepractice.problem_service.model.dto.internal;

import java.util.List;

import com.codepractice.problem_service.model.entity.TestCase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProblemClientTestCaseResponse {
    private String id;
    private List<TestCase> sampleTests;
}
