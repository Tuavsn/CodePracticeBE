package com.codepractice.problem_service.model.entity;

import com.codepractice.problem_service.enums.SubmitLanguage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CodeTemplate {
    private SubmitLanguage language;
    private String code;
}
