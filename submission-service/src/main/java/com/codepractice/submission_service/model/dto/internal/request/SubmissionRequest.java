package com.codepractice.submission_service.model.dto.internal.request;

import com.codepractice.submission_service.enums.SubmitLanguage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmissionRequest {
    private String userId;
    private String problemId;
    private String code;
    private SubmitLanguage language;
}
