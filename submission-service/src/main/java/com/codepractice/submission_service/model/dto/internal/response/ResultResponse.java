package com.codepractice.submission_service.model.dto.internal.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResultResponse {
    private String submissionId;
    private String result;
    private String error;
    private String stdout;
    private double time;
    private double memory;
    private double point;
}
