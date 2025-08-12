package com.codepractice.problem_service.model.entity;

import com.codepractice.problem_service.enums.SubmitLanguage;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CodeTemplate {
    @NotNull(message = "Language is required")
    private SubmitLanguage language;

    @NotBlank(message = "Code is required")
    @Size(max = 10000, message = "Code must not exceed 10000 characters")
    private String code;
}
