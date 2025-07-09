package com.codepractice.submission_service.model.dto.external;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProblemResponse {
    private String id;
    private List<TestCase> sampleTests;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TestCase {
        private String input;
        private String output;
        private double point;
    }
}
