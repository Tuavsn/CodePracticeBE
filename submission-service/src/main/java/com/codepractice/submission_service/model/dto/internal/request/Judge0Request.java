package com.codepractice.submission_service.model.dto.internal.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Judge0Request {
    private String source_code;
    private int language_id;
    private String stdin;
    private String expected_output;
}
