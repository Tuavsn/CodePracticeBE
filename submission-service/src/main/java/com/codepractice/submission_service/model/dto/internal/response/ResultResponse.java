package com.codepractice.submission_service.model.dto.internal.response;

import com.codepractice.submission_service.enums.SubmitResult;

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
    private SubmitResult result;
    private String error;
    private String stdout;
    private double time;
    private double memory;
    private double point;
}
