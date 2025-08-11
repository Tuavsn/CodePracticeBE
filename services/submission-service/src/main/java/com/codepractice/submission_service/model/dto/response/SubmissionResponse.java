package com.codepractice.submission_service.model.dto.response;

import java.time.LocalDateTime;

import com.codepractice.submission_service.enums.SubmitLanguage;
import com.codepractice.submission_service.enums.SubmitResult;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmissionResponse {
    private String id;
    private Long userId;
    private String problemId;
    private String code;
    private SubmitLanguage language;
    private SubmitResult result;
    private double time;
    private double memory;
    private double score;
    private LocalDateTime createdAt;
}
