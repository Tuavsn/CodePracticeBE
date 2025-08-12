package com.codepractice.submission_service.model.dto.request;

import com.codepractice.submission_service.enums.SubmitLanguage;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmissionRequest {
    @NotBlank(message = "Problem ID is required")
    private String problemId;

    @NotBlank(message = "Code is required")
    @Size(min = 1, max = 100000, message = "Code must be between 1 and 100000 characters")
    private String code;

    @NotNull(message = "Submit language is required")
    private SubmitLanguage language;
}
